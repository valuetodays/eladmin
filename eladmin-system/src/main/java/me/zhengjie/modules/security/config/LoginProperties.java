package me.zhengjie.modules.security.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * 配置文件读取
 *
 * @author liaojinlong
 * @since loginCode.length0loginCode.length0/6/10 17:loginCode.length6
 */
@ConfigMapping(prefix = "login")
@ApplicationScoped
public interface LoginProperties {

    @WithDefault("false")
    boolean singleLogin();

    CaptchaConfigProperty captcha();

    int userCacheIdleTime();

    public static final String cacheKey = "user-login-cache:";
}
