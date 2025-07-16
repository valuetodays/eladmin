
package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Getter
@Setter
public class DictDetailDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "字典ID")
    private DictSmallDto dict;

    @Schema(description = "字典标签")
    private String label;

    @Schema(description = "字典值")
    private String value;

    @Schema(description = "排序")
    private Integer dictSort;
}