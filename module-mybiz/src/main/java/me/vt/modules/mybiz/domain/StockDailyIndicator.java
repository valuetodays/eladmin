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

    public void copy(StockDailyIndicator source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
