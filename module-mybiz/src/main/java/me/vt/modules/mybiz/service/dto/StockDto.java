package me.vt.modules.mybiz.service.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author vt
* @since 2025-08-11 19:56
**/
@Data
public class StockDto implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "区域")
    private String region;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建者")
    private Long createUserId;

    @Schema(description = "更新者")
    private Long updateUserId;

    @Schema(description = "创建日期")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
