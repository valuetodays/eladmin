package me.vt.modules.mybiz.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ll.vt.api2.module.fortune.client.util.PriceUtilsEx;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorRealtimeCciReq;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorRealtimeCciResp;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.repository.StockDailyQuoteRepository;
import me.vt.modules.mybiz.service.ta4j.CciIndicatorHelper;
import me.vt.modules.mybiz.service.ta4j.Ta4jUtils;
import me.vt.utils.RedisUtils;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.ta4j.core.Bar;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.num.Num;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@Slf4j
@ApplicationScoped
public class StockDailyIndicatorRealtimeServiceImpl {
    private static final String CACHE_KEY_PREFIX_ = "rt:";

    @Inject
    StockDailyIndicatorServiceImpl stockDailyIndicatorService;
    @Inject
    StockDailyQuoteRepository stockDailyQuoteRepository;
    @Inject
    RedissonClient redissonClient;

    public List<StockDailyIndicatorRealtimeCciResp> realtimeCci(StockDailyIndicatorRealtimeCciReq req) {
        List<StockDailyIndicatorRealtimeCciResp> resps = new ArrayList<>();
        List<String> codes = req.getCodes();
        for (String code : codes) {
            StockDailyIndicatorRealtimeCciResp resp = computeRealtimeCci(code);
            CollectionUtils.addIgnoreNull(resps, resp);
        }
        return null;
    }

    public StockDailyIndicatorRealtimeCciResp computeRealtimeCci(String code) {
        final String name = CACHE_KEY_PREFIX_ + code;
        RBucket<List<StockDailyQuote>> bucket = redissonClient.getBucket(name);
        List<StockDailyQuote> cachedList = bucket.get();
        if (CollectionUtils.isEmpty(cachedList)) {
            cachedList = stockDailyQuoteRepository.findTop60ByCodeOrderByStatDateDesc(code);
        }
        if (CollectionUtils.isEmpty(cachedList)) {
            return null;
        } else {
            int offsetSeconds = LocalTime.of(15, 1).toSecondOfDay() - LocalTime.now().toSecondOfDay();
            if (offsetSeconds > 0) {
                bucket.set(cachedList, offsetSeconds, TimeUnit.SECONDS);
            }
        }

        // 需要使用“正序”来计算cci
        List<Bar> bars = cachedList.stream()
            .sorted(Comparator.comparing(StockDailyQuote::getStatDate))
            .map(e -> Ta4jUtils.buildBar(
                e.getStatDate(),
                e.getOpenVal(), e.getCloseVal(),
                e.getHighVal(), e.getLowVal(),
                e.getVolumeVal(), e.getAmountVal(),
                0))
            .toList();
        CCIIndicator cci14 = CciIndicatorHelper.buildCci(bars, code, null);

        int last = bars.size() - 1;
        Num value = cci14.getValue(last);
        LocalDate statDate = bars.get(last).getSystemZonedEndTime().toLocalDate();
        BigDecimal cci14BD = PriceUtilsEx.fixPrice(BigDecimal.valueOf(value.getDelegate().doubleValue()));
        StockDailyIndicatorRealtimeCciResp resp = new StockDailyIndicatorRealtimeCciResp();
        resp.setCode(code);
        resp.setCci14(cci14BD);
        return resp;
    }
}
