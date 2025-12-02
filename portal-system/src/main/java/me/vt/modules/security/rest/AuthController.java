package me.vt.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.vt.auth.AuthUser;
import cn.vt.auth.AuthUserHolder;
import cn.vt.encrypt.BCryptUtils;
import com.wf.captcha.base.Captcha;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.impl.CookieImpl;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.config.properties.RsaProperties;
import me.vt.exception.BadRequestException;
import me.vt.modules.security.config.CaptchaFactory;
import me.vt.modules.security.config.LoginProperties;
import me.vt.modules.security.config.enums.LoginCodeEnum;
import me.vt.modules.security.req.TokenInfoResp;
import me.vt.modules.security.security.TokenProvider;
import me.vt.modules.security.service.OnlineUserService;
import me.vt.modules.security.service.UserDetailsServiceImpl;
import me.vt.modules.security.service.dto.AuthUserDto;
import me.vt.modules.security.service.dto.JwtUserDto;
import me.vt.utils.RedisUtils;
import me.vt.utils.RsaUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/api/auth")
@Tag(name = "系统：系统授权接口")
public class AuthController extends BaseController {
    @Inject
    RedisUtils redisUtils;
    @Inject
    OnlineUserService onlineUserService;
    @Inject
    TokenProvider tokenProvider;
    @Inject
    LoginProperties loginProperties;
    @Inject
    CaptchaFactory captchaFactory;
    @Inject
    UserDetailsServiceImpl userDetailsService;
    @Inject
    RsaProperties rsaProperties;
    @Context
    HttpHeaders headers;

    @Log("用户登录")
    @Operation(summary = "用户登录")
    @Path(value = "/public/login")
    @POST
    public TokenInfoResp login(@Valid AuthUserDto authUser, @Context RoutingContext ctx) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), authUser.getPassword());
        // 查询验证码
        String code = redisUtils.get(authUser.getUuid(), String.class);
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // 获取用户信息
        JwtUserDto jwtUser = userDetailsService.loadUserByUsername(authUser.getUsername());
        // 验证用户密码
        if (!BCryptUtils.checkpw(password, jwtUser.getPassword())) {
            throw new BadRequestException("账号或密码错误");
        }

        TokenInfoResp tokenInfoResp = new TokenInfoResp();
        tokenInfoResp.setUser(jwtUser);
        refreshTokenAndAddToResponse(ctx, jwtUser, tokenInfoResp);
        if (loginProperties.singleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(authUser.getUsername());
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, tokenInfoResp.getToken(), headers.getHeaderString("User-Agent"), getIp());
        // 返回登录信息
        return tokenInfoResp;
    }


    private void refreshTokenAndAddToResponse(RoutingContext ctx, JwtUserDto jwtUser,
            TokenInfoResp tokenInfoVO) {
        // 生成令牌
        String token = tokenProvider.createToken();
        tokenInfoVO.setToken(token);
        AuthUser authUser = new AuthUser();
        authUser.setUserId(String.valueOf(jwtUser.getUser().getId()));
        authUser.setEmail(jwtUser.getUser().getEmail());
        authUser.setLoginToken(token);
        super.putLoginAccount(authUser);

        String scheme = ctx.request().scheme(); // http 或 https
        CookieImpl cookieToAdd = new CookieImpl("portal_" + AuthUserHolder.AUTH_HEADER_KEY, token);
        cookieToAdd.setDomain(".valuetodays.xyz") // TODO how to get domain in request
                .setPath("/").setMaxAge(Duration.ofDays(7).toSeconds()).setHttpOnly(true);
        if (scheme.equalsIgnoreCase("https")) {
            cookieToAdd.setSecure(true).setSameSite(CookieSameSite.NONE);
        } else {
            cookieToAdd.setSameSite(CookieSameSite.LAX);
        }

        String host = ctx.request().host();
        String domain = host.contains(":") ? host.split(":")[0] : host;
        if (domain.endsWith("valuetodays.xyz")) {
            cookieToAdd.setDomain(".valuetodays.xyz");
        }

        ctx.response().putHeader(AuthUserHolder.AUTH_HEADER_KEY, token).addCookie(cookieToAdd);
    }

    @Operation(summary = "获取用户信息")
    @POST
    @Path(value = "/info")
    public JwtUserDto getUserInfo() {
        AuthUser currentAccount = getCurrentAccount();
        String username = currentAccount.getEmail();
        return userDetailsService.loadUserByUsername(username);
    }

    @Operation(summary = "获取验证码")
    @Path(value = "/public/code")
    @POST
    public Object getCode() {
        // 获取运算的结果
        Captcha captcha = captchaFactory.getCaptcha();
        String uuid = "captcha_code:" + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.captcha().expiration(), TimeUnit.MINUTES);
        // 验证码信息
        return Map.of("img", captcha.toBase64(), "uuid", uuid);
    }

    @Operation(summary = "退出登录")
    @Path(value = "/logout")
    @POST
    public Object logout() {
        onlineUserService.logout(tokenProvider.getToken());
        return 1;
    }
}
