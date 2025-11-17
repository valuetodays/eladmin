package me.vt.modules.mybiz.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-11-17 19:01
**/
@Data
public class StockDailyQuoteDto implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "统计日期")
    private LocalDate statDate;

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
