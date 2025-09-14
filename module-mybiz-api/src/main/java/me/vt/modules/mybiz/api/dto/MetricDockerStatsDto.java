package me.vt.modules.mybiz.api.dto;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@Data
public class MetricDockerStatsDto implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "统计时间")
    private LocalDateTime statDatetime;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "a")
    private String blockIo;

    @Schema(description = "b")
    private String netIo;

    @Schema(description = "c")
    private String cpuPerc;

    @Schema(description = "d")
    private String containerId;

    @Schema(description = "ee")
    private String memPerc;

    @Schema(description = "f")
    private String memUsage;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改日期")
    private LocalDateTime updateTime;

    @Schema(description = "创建者id")
    private Long createUserId;

    @Schema(description = "更新者id")
    private Long updateUserId;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "mem_usage")
    private BigDecimal memUsage2;
}
