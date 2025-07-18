package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.base.BaseDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "菜单")
    private Set<MenuDto> menus;

    @Schema(description = "部门")
    private Set<DeptDto> depts;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "数据权限")
    private String dataScope;

    @Schema(description = "级别")
    private Integer level;

    @Schema(description = "描述")
    private String description;

}
