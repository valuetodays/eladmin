package me.vt.modules.mybiz.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.vt.util.JsonUtils;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.StockInfoDto;
import me.vt.modules.mybiz.domain.StockInfoPersist;
import me.vt.modules.mybiz.service.mapstruct.StockInfoConverter;
import org.junit.jupiter.api.Test;

/**

* Tests for {@link StockInfoServiceImpl}.
* @author lei.liu
* @since 2026-02-10
*/
@QuarkusTest
@Slf4j
class StockInfoServiceImplTest {

    @Inject
    StockInfoServiceImpl stockInfoService;
    @Inject
    RedisDataSource redisDataSource;
    @Inject
    StockDailyQuoteServiceImpl stockDailyQuoteService;
    @Inject
    StockInfoConverter stockInfoConverter;

    @Test
    void save() {
        String key = "etfinfo";
        HashCommands<String, String, String> hashCommands = redisDataSource.hash(String.class, String.class, String.class);
        Map<String, String> hgetall = hashCommands.hgetall(key);
        for (Map.Entry<String, String> entry : hgetall.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            log.info("> {} -> {}", field, value);
            StockInfoPersist toSave = JsonUtils.fromJson(value, StockInfoPersist.class);
            toSave.setCode(field);
            toSave.setRegion(field.startsWith("1")?"SZ":"SH");
            toSave.setPopularFlag(false);
            toSave.setT0Flag(true);
            toSave.setCreateTime(LocalDateTime.now());
            toSave.setUpdateTime(LocalDateTime.now());
            toSave.setCreateUserId(1L);
            toSave.setUpdateUserId(1L);
            stockInfoService.create(toSave);
        }
    }

    @Test
    void saveAllDailyQutoe() {
        List<StockInfoDto> popularList = stockInfoService.findPopularList();
        for (StockInfoDto stockInfoDto : popularList) {
            StockInfoPersist persist = stockInfoConverter.toEntity(stockInfoDto);
            stockDailyQuoteService.getAndSaveToDb(persist, true);
        }
    }

}
