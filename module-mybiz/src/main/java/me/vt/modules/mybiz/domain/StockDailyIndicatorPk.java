package me.vt.modules.mybiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-18
 */
@Data
@Embeddable
public class StockDailyIndicatorPk implements Serializable {
    @Column(name = "code")
    @Schema(description = "code")
    private String code;

    @Column(name = "stat_date")
    @Schema(description = "统计日期")
    private LocalDate statDate;
}
