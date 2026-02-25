package me.vt.modules.mybiz.service;

import java.sql.SQLException;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@QuarkusTest
@Slf4j
public class StockDailyIndicatorServiceImplTest {
    @Inject
    StockDailyIndicatorServiceImpl stockDailyIndicatorService;

    @Test
    public void testStockDailyIndicatorRealtimeService() throws SQLException {
        stockDailyIndicatorService.computeKdj("159901", false);
    }
}
