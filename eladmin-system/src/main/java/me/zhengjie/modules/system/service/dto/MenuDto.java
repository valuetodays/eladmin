package me.zhengjie.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Zheng Jie
 * @since 2018-12-17
 */
@Getter
@Setter
public class MenuDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "子节点")
    private List<MenuDto> children;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "权限")
    private String permission;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "排序")
    private Integer menuSort;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "PID")
    private Long pid;

    @Schema(description = "子节点数目")
    private Integer subCount;

    @Schema(description = "是否为Iframe")
    @JsonProperty("iFrame")
    private Boolean iFrame;

    @Schema(description = "是否缓存")
    private Boolean cache;

    @Schema(description = "是否隐藏")
    private Boolean hidden;

    @Schema(description = "组件名称")
    private String componentName;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否存在子节点")
    public Boolean getHasChildren() {
        return subCount > 0;
    }

    @Schema(description = "是否叶子节点")
    public Boolean getLeaf() {
        return subCount <= 0;
    }

    @Schema(description = "标题")
    public String getLabel() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
