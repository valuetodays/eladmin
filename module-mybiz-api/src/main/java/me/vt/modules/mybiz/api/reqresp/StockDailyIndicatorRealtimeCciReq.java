package me.vt.modules.mybiz.api.reqresp;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@Data
public class StockDailyIndicatorRealtimeCciReq implements Serializable {
    @NotEmpty
    private List<String> codes;
}
