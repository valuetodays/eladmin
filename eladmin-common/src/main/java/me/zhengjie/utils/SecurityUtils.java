package me.zhengjie.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.valuetodays.quarkus.commons.base.BaseAuthorizationController;
import cn.vt.auth.AuthUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 获取当前登录的用户
 * @author Zheng Jie
 * @since 2019-01-17
 */
@Slf4j
@ApplicationScoped
public class SecurityUtils extends BaseAuthorizationController {
    @Context
    HttpHeaders headers;

    public static String header;

    public static String tokenStartWith;

    //    @Value("${jwt.header}")
    public void setHeader(String header) {
        SecurityUtils.header = header;
    }

    //    @Value("${jwt.token-start-with}")
    public void setTokenStartWith(String tokenStartWith) {
        SecurityUtils.tokenStartWith = tokenStartWith;
    }

    /**
     * 获取当前登录的用户
     * @return UserDetails
     */
    public AuthUser getCurrentUser() {
        // 从header中取出token，再从redis中取出用户信息
        AuthUser currentAccount = super.getCurrentAccount();
        return currentAccount;
    }

    /**
     * 获取当前用户的数据权限
     * @return /
     */
    public List<Long> getCurrentUserDataScope() {
        AuthUser userDetails = getCurrentUser();
        // 将 Java 对象转换为 JSONObject 对象
//        JSONObject jsonObject = (JSONObject) JSON.toJSON(userDetails);
//        JSONArray jsonArray = jsonObject.getJSONArray("dataScopes");
//        return JSON.parseArray(jsonArray.toJSONString(), Long.class);
        return List.of(); // fixme
    }

    /**
     * 获取用户ID
     * @return 系统用户ID
     */
    public Long getCurrentUserId() {
        return getCurrentUserId(getToken());
    }

    /**
     * 获取用户ID
     * @return 系统用户ID
     */
    public Long getCurrentUserId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return Long.valueOf(jwt.getPayload("userId").toString());
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public String getCurrentUsername() {
        return getCurrentUsername(getToken());
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayload("sub").toString();
    }

    /**
     * 获取Token
     * @return /
     */
    public String getToken() {
        String bearerToken = headers.getHeaderString(header);
        if (bearerToken != null && bearerToken.startsWith(tokenStartWith)) {
            // 去掉令牌前缀
            return bearerToken.replace(tokenStartWith, "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
