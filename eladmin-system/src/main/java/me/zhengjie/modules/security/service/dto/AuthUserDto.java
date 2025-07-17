package me.zhengjie.modules.security.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


/**
 * @author Zheng Jie
 * @since 2018-11-30
 */
@Getter
@Setter
public class AuthUserDto {

    @NotBlank
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "验证码的key")
    private String uuid = "";
}
