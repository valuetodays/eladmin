package me.zhengjie.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@Getter
@Setter
public class DictSmallDto implements Serializable {

    @Schema(description = "ID")
    private Long id;
}
