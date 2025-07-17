package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author Zheng Jie
 * @since 2019-6-10 16:32:18
*/
@Data
@NoArgsConstructor
public class JobSmallDto implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;
}