package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-11-18 20:01
**/
@Entity
@Data
@Table(name = "f_stock_daily_indicator")
public class StockDailyIndicator implements Serializable {

    @Column(name = "code")
    @Schema(description = "code")
    private String code;

    @Column(name = "stat_date")
    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Column(name = "cci14", nullable = false)
    @NotNull
    @Schema(description = "cci14")
    private BigDecimal cci14;

    public void copy(StockDailyIndicator source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
