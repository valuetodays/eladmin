package me.vt.modules.mybiz.service.ta4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.vt.modules.mybiz.service.ta4j.pojo.RealtimeStockQuoteVo;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.bars.TimeBarBuilderFactory;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.num.DecimalNumFactory;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
public final class CciIndicatorHelper {
    private CciIndicatorHelper() {
    }

    /**
     * 构建 cci指标，可以是历史，也可以是实时
     * @param ascBars 正序的k线数据
     * @param code code
     * @param realtimeStockQuote 实时k线数据，可以为null
     */
    public static CCIIndicator buildCci(List<Bar> ascBars, String code, RealtimeStockQuoteVo realtimeStockQuote) {
        List<Bar> barsToUse = new ArrayList<>(ascBars);
        if (Objects.nonNull(realtimeStockQuote)) {
            Bar realtimeBar = Ta4jUtils.buildBar(
                LocalDate.now(),
                realtimeStockQuote.getOpenVal(),
                realtimeStockQuote.getCloseVal(),
                realtimeStockQuote.getHighVal(),
                realtimeStockQuote.getLowVal(),
                realtimeStockQuote.getVolumeVal(),
                realtimeStockQuote.getAmountVal(),
                0
            );

            LocalDate today = realtimeBar.getSystemZonedEndTime().toLocalDate();
            LocalDate lastDate = barsToUse.getLast()
                .getSystemZonedEndTime()
                .toLocalDate();

            if (lastDate.equals(today)) {
                barsToUse.set(barsToUse.size()-1, realtimeBar);
            } else {
                barsToUse.add(realtimeBar);
            }
        }

        BaseBarSeriesBuilder baseBarSeriesBuilder = new BaseBarSeriesBuilder();
        baseBarSeriesBuilder.withName(code)
            .withBars(barsToUse)
            .withNumFactory(DecimalNumFactory.getInstance(3))
            .withBarBuilderFactory(new TimeBarBuilderFactory());
        BaseBarSeries baseBarSeries = baseBarSeriesBuilder.build();
        CCIIndicator cci14 = new CCIIndicator(
            baseBarSeries, // 基于TP计算CCI
            14 // 周期N=14
        );
        return cci14;
    }
}
