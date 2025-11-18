package me.vt.modules.mybiz.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-11-18 20:01
**/
@Data
public class StockDailyIndicatorDto implements Serializable {

    @Schema(description = "code")
    private String code;

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "cci14")
    private BigDecimal cci14;
}
