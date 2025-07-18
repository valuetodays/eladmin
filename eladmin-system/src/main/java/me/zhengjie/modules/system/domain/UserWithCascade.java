package me.zhengjie.modules.system.domain;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@Data
public class UserWithCascade {
    @Schema(description = "用户部门")
    private Dept dept;

    @ManyToMany(fetch = FetchType.EAGER)
    @Schema(description = "用户角色")
    @JoinTable(name = "sys_users_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @Schema(description = "用户岗位")
    @JoinTable(name = "sys_users_jobs",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "job_id")})
    private Set<Job> jobs;
}
