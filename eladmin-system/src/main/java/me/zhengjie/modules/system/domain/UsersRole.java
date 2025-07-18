package me.zhengjie.modules.system.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@Entity
@Data
@Table(name = "sys_users_roles")
public class UsersRole implements Serializable {
    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", hidden = true)
    private Long id;

    private Long userId;
    private Long roleId;
}
