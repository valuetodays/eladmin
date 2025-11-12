package me.vt.modules.mybiz.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@Data
public class IndexInfoDto implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "区域（上海，深圳，北京，香港）")
    private String region;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "发布日期")
    private LocalDate releaseDate;

    @Schema(description = "数据基准日期")
    private LocalDate dataBaseDate;

    @Schema(description = "是否常见")
    private Boolean popularFlag;

    @Schema(description = "建议的etf列表")
    private String suggestEtfs;

    @Schema(description = "调仓频率")
    private String adjFreq;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改日期")
    private LocalDateTime updateTime;

    @Schema(description = "创建者id")
    private Long createUserId;

    @Schema(description = "更新者id")
    private Long updateUserId;
}
