package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @EmbeddedId
    public StockDailyIndicatorPk id;

    @Column(name = "cci14")
    @Schema(description = "cci14")
    private BigDecimal cci14;

    @Column(name = "kdj_k")
    @Schema(description = "kdj_k")
    private BigDecimal kdjK;

    @Column(name = "kdj_d")
    @Schema(description = "kdj_d")
    private BigDecimal kdjD;

    @Column(name = "kdj_j")
    @Schema(description = "kdj_j")
    private BigDecimal kdjJ;

    @Column(name = "ma5")
    @Schema(description = "ma5")
    private BigDecimal ma5;

    @Column(name = "ma20")
    @Schema(description = "ma20")
    private BigDecimal ma20;

    @Column(name = "ma30")
    @Schema(description = "ma30")
    private BigDecimal ma30;

    @Column(name = "ma60")
    @Schema(description = "ma60")
    private BigDecimal ma60;

    @Column(name = "ma120")
    @Schema(description = "ma120")
    private BigDecimal ma120;

    @Column(name = "ma240")
    @Schema(description = "ma240")
    private BigDecimal ma240;

    @Column(name = "ma5_volume")
    @Schema(description = "ma5 volume")
    private BigDecimal ma5Volume;

    @Column(name = "ma20_volume")
    @Schema(description = "ma20 volume")
    private BigDecimal ma20Volume;

    public void copy(StockDailyIndicator source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
