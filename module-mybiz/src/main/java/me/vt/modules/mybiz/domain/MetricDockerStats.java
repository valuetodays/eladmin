package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.valuetodays.quarkus.commons.base.jpa.JpaCrudLongIdBasePersist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "extra_metric_docker_stats")
public class MetricDockerStats extends JpaCrudLongIdBasePersist {

    @Column(name = "stat_datetime", nullable = false)
    @NotNull
    @Schema(description = "统计时间")
    private LocalDateTime statDatetime;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Column(name = "block_io", nullable = false)
    @NotBlank
    @Schema(description = "a")
    private String blockIo;

    @Column(name = "net_io", nullable = false)
    @NotBlank
    @Schema(description = "b")
    private String netIo;

    @Column(name = "cpu_perc", nullable = false)
    @NotBlank
    @Schema(description = "c")
    private String cpuPerc;

    @Column(name = "container_id", nullable = false)
    @NotBlank
    @Schema(description = "d")
    private String containerId;

    @Column(name = "mem_perc", nullable = false)
    @NotBlank
    @Schema(description = "ee")
    private String memPerc;

    @Column(name = "mem_usage", nullable = false)
    @NotBlank
    @Schema(description = "f")
    private String memUsage;

    @Column(name = "ip")
    @Schema(description = "ip")
    private String ip;

    @Column(name = "mem_usage2", nullable = false)
    @NotNull
    @Schema(description = "mem_usage")
    private BigDecimal memUsage2;

    public void copy(MetricDockerStats source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
