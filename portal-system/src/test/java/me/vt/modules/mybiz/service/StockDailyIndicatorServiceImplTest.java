package me.vt.modules.mybiz.service;

import java.sql.SQLException;
import java.util.List;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.StockInfoDto;
import org.junit.jupiter.api.Disabled;
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
    @Inject
    StockInfoServiceImpl stockInfoService;

    @Test
    public void testStockDailyIndicatorRealtimeService() throws SQLException {
        stockDailyIndicatorService.computeKdj("159901", false);
    }

    @Test
    public void testMa() throws SQLException {
        stockDailyIndicatorService.computeMa("513300", true);
    }

    @Test
    @Disabled
    public void computeAllMa() throws SQLException {
        List<StockInfoDto> popularList = stockInfoService.findPopularList();
        for (StockInfoDto stockInfoDto : popularList) {
//            stockDailyIndicatorService.computeCci(stockInfoDto.getCode(), true);
//            stockDailyIndicatorService.computeKdj(stockInfoDto.getCode(), true);
            stockDailyIndicatorService.computeMa(stockInfoDto.getCode(), true);
        }
    }
}
