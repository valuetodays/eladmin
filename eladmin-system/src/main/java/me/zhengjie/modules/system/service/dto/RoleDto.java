package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleDto roleDto = (RoleDto) o;
        return Objects.equals(id, roleDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
