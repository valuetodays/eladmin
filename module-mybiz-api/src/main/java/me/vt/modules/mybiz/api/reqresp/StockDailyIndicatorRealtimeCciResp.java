package me.vt.modules.mybiz.api.reqresp;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author valuetodays
* @since 2025-11-18 20:01
**/
@Data
public class StockDailyIndicatorRealtimeCciResp implements Serializable {
    @Schema(description = "code")
    private String code;

    @Schema(description = "name")
    private String name;

    @Schema(description = "cci14")
    private BigDecimal cci14;
}
