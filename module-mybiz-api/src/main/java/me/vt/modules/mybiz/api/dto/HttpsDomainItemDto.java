package me.vt.modules.mybiz.api.dto;

import lombok.Data;
import java.io.Serializable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@Data
public class HttpsDomainItemDto implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;
}
