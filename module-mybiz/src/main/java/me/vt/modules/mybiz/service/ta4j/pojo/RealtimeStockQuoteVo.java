package me.vt.modules.mybiz.service.ta4j.pojo;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@Data
public final class RealtimeStockQuoteVo implements Serializable {
    private String code;

    @Schema(description = "开盘点数")
    private BigDecimal openVal;

    @Schema(description = "收盘点数")
    private BigDecimal closeVal;

    @Schema(description = "最高点数")
    private BigDecimal highVal;

    @Schema(description = "最低点数")
    private BigDecimal lowVal;

    @Schema(description = "成交量")
    private BigDecimal volumeVal;

    @Schema(description = "成交额")
    private BigDecimal amountVal;
}
