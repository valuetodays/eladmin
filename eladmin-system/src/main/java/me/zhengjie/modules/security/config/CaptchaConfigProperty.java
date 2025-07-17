package me.zhengjie.modules.security.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.modules.security.config.enums.LoginCodeEnum;

/**
 * 验证码配置映射
 */
@ConfigMapping(prefix = "login.code")
@StaticInitSafe
@ApplicationScoped
public interface CaptchaConfigProperty {

    /**
     * 验证码类型
     */
    LoginCodeEnum codeType();

    /**
     * 验证码有效期（分钟）
     */
    @WithDefault("5")
    Long expiration();

    /**
     * 验证码长度
     */
    @WithDefault("4")
    int length();

    /**
     * 验证码宽度
     */
    @WithDefault("111")
    int width();

    /**
     * 验证码高度
     */
    @WithDefault("36")
    int height();

    /**
     * 验证码字体名称（可为空）
     */
    String fontName();

    /**
     * 字体大小
     */
    @WithDefault("25")
    int fontSize();
}