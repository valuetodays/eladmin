package me.zhengjie.modules.system.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Date;

/**
 * @author Zheng Jie
 * @since 2018-11-22
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="sys_user")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", hidden = true)
    private Long id;

    @Column(name = "dept_id")
    private Long deptId;

    @NotBlank
    @Column(unique = true)
    @Schema(description = "用户名称")
    private String username;

    @NotBlank
    @Schema(description = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @Schema(description = "邮箱")
    private String email;

    @NotBlank
    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "头像真实名称", hidden = true)
    private String avatarName;

    @Schema(description = "头像存储的路径", hidden = true)
    private String avatarPath;

    @Schema(description = "密码")
    private String password;

    @NotNull
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "是否为admin账号", hidden = true)
    private Boolean isAdmin = false;

    @Column(name = "pwd_reset_time")
    @Schema(description = "最后修改密码的时间", hidden = true)
    private Date pwdResetTime;
}
