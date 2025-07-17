package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@Getter
@Setter
public class DeptDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "排序")
    private Integer deptSort;

    @Schema(description = "子部门")
    private List<DeptDto> children;

    @Schema(description = "上级部门")
    private Long pid;

    @Schema(description = "子部门数量", hidden = true)
    private Integer subCount;

    @Schema(description = "是否有子节点")
    public Boolean getHasChildren() {
        return subCount > 0;
    }

    @Schema(description = "是否为叶子")
    public Boolean getLeaf() {
        return subCount <= 0;
    }

    @Schema(description = "部门全名")
    public String getLabel() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeptDto deptDto = (DeptDto) o;
        return Objects.equals(id, deptDto.id) &&
                Objects.equals(name, deptDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}