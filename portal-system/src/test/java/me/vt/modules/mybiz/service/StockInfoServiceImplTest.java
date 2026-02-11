package me.vt.modules.mybiz.service;

import cn.vt.util.JsonUtils;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.StockInfoDto;
import me.vt.modules.mybiz.domain.StockInfoPersist;
import me.vt.modules.mybiz.service.mapstruct.StockInfoConverter;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

/**

* Tests for {@link StockInfoServiceImpl}.
* @author lei.liu
* @since 2026-02-10
*/
@QuarkusTest
@Slf4j
public class StockInfoServiceImplTest {

    @Inject
    StockInfoServiceImpl stockInfoService;
    @Inject
    RedissonClient redissonClient;
    @Inject
    StockDailyQuoteServiceImpl stockDailyQuoteService;
    @Inject
    StockInfoConverter stockInfoConverter;

    @Test
    void save() {
        String key = "etfinfo";
        RMap<String, String> hash = redissonClient.getMap(key);
        Map<String, String> hgetall = hash.readAllMap();
        for (Map.Entry<String, String> entry : hgetall.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            log.info("> {} -> {}", field, value);
            StockInfoPersist toSave = JsonUtils.fromJson(value, StockInfoPersist.class);
            toSave.setCode(field);
            toSave.setRegion(field.startsWith("1") ? "SZ" : "SH");
            toSave.setPopularFlag(false);
            toSave.setT0Flag(false);
            String shortName = toSave.getShortName();
            if (StringUtils.isBlank(shortName)) {
                toSave.setShortName("-");
            }
            BigDecimal manageRadio = toSave.getManageRadio();
            if (Objects.isNull(manageRadio)) {
                toSave.setManageRadio(BigDecimal.valueOf(-1));
                toSave.setHolderRadio(BigDecimal.valueOf(-1));
                toSave.setSellRadio(BigDecimal.valueOf(-1));
            }
            String busiCompareBase = toSave.getBusiCompareBase();
            if (StringUtils.isBlank(busiCompareBase)) {
                toSave.setBusiCompareBase("-");
            }
            String followIndex = toSave.getFollowIndex();
            if (StringUtils.isBlank(followIndex)) {
                toSave.setFollowIndex("-");
            }
            toSave.setCreateTime(LocalDateTime.now());
            toSave.setUpdateTime(LocalDateTime.now());
            toSave.setCreateUserId(1L);
            toSave.setUpdateUserId(1L);
            stockInfoService.create(toSave);
            hash.remove(field);
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
