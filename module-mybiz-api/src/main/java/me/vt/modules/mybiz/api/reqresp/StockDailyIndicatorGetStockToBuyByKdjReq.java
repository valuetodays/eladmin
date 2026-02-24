package me.vt.modules.mybiz.api.reqresp;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@Data
public class StockDailyIndicatorGetStockToBuyByKdjReq implements Serializable {
    @NotNull
    private LocalDate statDate;
    private boolean pushMsg = false;
}
