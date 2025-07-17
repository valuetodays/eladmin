package me.zhengjie.modules.security.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
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
@StaticInitSafe
@ApplicationScoped
public interface LoginProperties {

    /**
     * 账号单用户 登录
     */
    @WithDefault("false")
    boolean singleLogin();

    public static final String cacheKey = "user-login-cache:";
}
