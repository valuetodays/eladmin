package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author Zheng Jie
 * @since 2019-6-10 16:32:18
*/
@Data
public class DeptSmallDto implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;
}