package me.zhengjie.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
 * 邮件配置类，数据存覆盖式存入数据存
 * @author Zheng Jie
 * @since 2018-12-26
 */
@Entity
@Data
@Table(name = "tool_email_config")
public class EmailConfig implements Serializable {

    @Id
    @Column(name = "config_id")
    @Schema(description = "ID", hidden = true)
    private Long id;

    @NotBlank
    @Schema(description = "邮件服务器SMTP地址")
    private String host;

    @NotBlank
    @Schema(description = "邮件服务器 SMTP 端口")
    private String port;

    @NotBlank
    @Schema(description = "发件者用户名")
    private String user;

    @NotBlank
    @Schema(description = "密码")
    private String pass;

    @NotBlank
    @Schema(description = "收件人")
    private String fromUser;
}
