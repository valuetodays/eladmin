package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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
