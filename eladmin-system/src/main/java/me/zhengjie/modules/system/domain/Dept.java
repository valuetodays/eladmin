package me.zhengjie.modules.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.Set;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="sys_dept")
public class Dept extends BaseEntity implements Serializable {

    @Id
    @Column(name = "dept_id")
    @NotNull(groups = Update.class)
    @Schema(description = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "depts")
    @Schema(description = "角色")
    private Set<Role> roles;

    @Schema(description = "排序")
    private Integer deptSort;

    @NotBlank
    @Schema(description = "部门名称")
    private String name;

    @NotNull
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "上级部门")
    private Long pid;

    @Schema(description = "子节点数目", hidden = true)
    private Integer subCount = 0;

}