package me.vt.modules.mybiz.service;
import java.util.List;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorRealtimeCciReq;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorRealtimeCciResp;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@QuarkusTest
@Slf4j
public class StockDailyIndicatorRealtimeServiceImplTest {
    @Inject
    StockDailyIndicatorRealtimeServiceImpl stockDailyIndicatorRealtimeService;

    @Test
    void test() {
        var req = new StockDailyIndicatorRealtimeCciReq();
        req.setCodes(List.of("512070"));
        List<StockDailyIndicatorRealtimeCciResp> resps = stockDailyIndicatorRealtimeService.realtimeCci(req);
        log.info("resps={}", resps);
    }
}
