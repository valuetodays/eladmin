
package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Getter
@Setter
@NoArgsConstructor
public class JobDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "岗位排序")
    private Integer jobSort;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否启用")
    private Boolean enabled;

    public JobDto(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}