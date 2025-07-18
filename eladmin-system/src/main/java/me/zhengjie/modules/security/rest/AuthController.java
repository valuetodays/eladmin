package me.zhengjie.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.vt.auth.AuthUser;
import cn.vt.encrypt.BCryptUtils;
import com.wf.captcha.base.Captcha;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.config.CaptchaFactory;
import me.zhengjie.modules.security.config.LoginProperties;
import me.zhengjie.modules.security.config.enums.LoginCodeEnum;
import me.zhengjie.modules.security.security.TokenProvider;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.security.service.UserDetailsServiceImpl;
import me.zhengjie.modules.security.service.dto.AuthUserDto;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.utils.RedisUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/auth")
@RequiredArgsConstructor
@Tag(name = "系统：系统授权接口")
public class AuthController extends BaseController {
    //    @Inject
//    SecurityProperties properties;
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

    @Log("用户登录")
    @Operation(summary = "用户登录")
    @Path(value = "/login")
    @POST
    public Object login(@Valid AuthUserDto authUser) throws Exception {
        // 密码解密
//        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        String password = authUser.getPassword();
//        // 查询验证码
//        String code = redisUtils.get(authUser.getUuid(), String.class);
//        // 清除验证码
//        redisUtils.del(authUser.getUuid());
//        if (StringUtils.isBlank(code)) {
//            throw new BadRequestException("验证码不存在或已过期");
//        }
//        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
//            throw new BadRequestException("验证码错误");
//        }
        // 获取用户信息
        JwtUserDto jwtUser = userDetailsService.loadUserByUsername(authUser.getUsername());
        // 验证用户密码
        if (!BCryptUtils.checkpw(password, jwtUser.getPassword())) {
            throw new BadRequestException("登录密码错误");
        }
        // fixme:
        putLoginAccount(null);
        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", token);
            put("user", jwtUser);
        }};
        if (loginProperties.singleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(authUser.getUsername());
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, token);
        // 返回登录信息
        return authInfo;
    }

    @Operation(summary = "获取用户信息")
    @GET
    @Path(value = "/info")
    public AuthUser getUserInfo() {
        AuthUser currentAccount = getCurrentAccount();
        return currentAccount;
    }

    @Operation(summary = "获取验证码")
    @Path(value = "/code")
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
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return imgResult;
    }

    @Operation(summary = "退出登录")
    @Path(value = "/logout")
    @POST
    public Object logout() {
        onlineUserService.logout(tokenProvider.getToken());
        return 1;
    }
}
