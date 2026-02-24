package me.vt.modules.mybiz.api.reqresp;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@Data
public class StockDailyIndicatorGetStockToBuyByKdjResp implements Serializable {
    @Schema(description = "code")
    private String code;

    @Schema(description = "name")
    private String name;

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "k")
    private BigDecimal k;
    @Schema(description = "d")
    private BigDecimal d;
    @Schema(description = "j")
    private BigDecimal j;
}
