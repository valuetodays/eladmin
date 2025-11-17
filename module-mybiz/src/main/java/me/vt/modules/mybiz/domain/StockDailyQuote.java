package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-11-17 19:01
**/
@Entity
@Data
@Table(name = "f_stock_daily_quote")
public class StockDailyQuote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    @NotBlank
    @Schema(description = "编号")
    private String code;

    @Column(name = "stat_date", nullable = false, columnDefinition = "date")
    @NotNull
    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Column(name = "open_val", nullable = false)
    @NotNull
    @Schema(description = "开盘点数")
    private BigDecimal openVal;

    @Column(name = "close_val", nullable = false)
    @NotNull
    @Schema(description = "收盘点数")
    private BigDecimal closeVal;

    @Column(name = "high_val", nullable = false)
    @NotNull
    @Schema(description = "最高点数")
    private BigDecimal highVal;

    @Column(name = "low_val", nullable = false)
    @NotNull
    @Schema(description = "最低点数")
    private BigDecimal lowVal;

    @Column(name = "volume_val", nullable = false)
    @NotNull
    @Schema(description = "成交量")
    private BigDecimal volumeVal;

    @Column(name = "amount_val", nullable = false)
    @NotNull
    @Schema(description = "成交额")
    private BigDecimal amountVal;

    public void copy(StockDailyQuote source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
