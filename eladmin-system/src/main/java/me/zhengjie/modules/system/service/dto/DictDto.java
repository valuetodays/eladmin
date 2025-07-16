
package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Getter
@Setter
public class DictDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "字典详情")
    private List<DictDetailDto> dictDetails;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private String description;
}
