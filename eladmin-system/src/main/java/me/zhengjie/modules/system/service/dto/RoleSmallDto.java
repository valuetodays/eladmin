package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@Data
public class RoleSmallDto implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "级别")
    private Integer level;

    @Schema(description = "数据权限")
    private String dataScope;
}
