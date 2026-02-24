package me.vt.modules.mybiz.service.ta4j;

import java.sql.SQLException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.service.ta4j.pojo.KdjContextResp;
import org.apache.commons.collections4.CollectionUtils;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.bars.TimeBarBuilderFactory;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.DecimalNumFactory;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-24
 */
@Slf4j
public final class KdjIndicatorHelper {
    private KdjIndicatorHelper() {
    }

    /**
     *
     * @param stockDailyQuotes datalist
     * @param rsvPeriod 定义KDJ参数（国内通用9,3,3）
     */
    public static KdjContextResp computeKdj(List<StockDailyQuote> stockDailyQuotes, int rsvPeriod) {
        // 1. 获取日K数据
        if (CollectionUtils.isEmpty(stockDailyQuotes)) {
            return null;
        }
        String code = stockDailyQuotes.getFirst().getCode();
        List<Bar> bars = stockDailyQuotes.stream().map(e -> Ta4jUtils.buildBar(
            e.getStatDate(),
            e.getOpenVal(), e.getCloseVal(),
            e.getHighVal(), e.getLowVal(),
            e.getVolumeVal(), e.getAmountVal(),
            0
        )).toList();

        // 2. 构建BarSeries
        BaseBarSeriesBuilder baseBarSeriesBuilder = new BaseBarSeriesBuilder();
        BaseBarSeries series = baseBarSeriesBuilder.withName(code)
            .withBars(bars)
            .withNumFactory(DecimalNumFactory.getInstance())
            .withBarBuilderFactory(new TimeBarBuilderFactory())
            .build();

        // 3. 定义KDJ参数（国内通用9,3,3）
//        int rsvPeriod = 9;
        int kSmoothPeriod = 3;
        int dSmoothPeriod = 3;
        NumFactory numFactory = DecimalNumFactory.getInstance();

        // ==============================
        // 核心：按国内公式手动计算KDJ（和交易软件对齐）
        // ==============================
        // 3.1 计算RSV：(今日收盘价 - 9日最低价)/(9日最高价 - 9日最低价) * 100
        HighPriceIndicator highIndicator = new HighPriceIndicator(series);
        LowPriceIndicator lowIndicator = new LowPriceIndicator(series);
        ClosePriceIndicator closeIndicator = new ClosePriceIndicator(series);

        // 计算N周期最高价/最低价
        Indicator<Num> highestHigh = new HighestValueIndicator(highIndicator, rsvPeriod);
        Indicator<Num> lowestLow = new LowestValueIndicator(lowIndicator, rsvPeriod);

        // 计算RSV（处理分母为0的情况，避免除0异常）
        Indicator<Num> rsv = new CachedIndicator<Num>(closeIndicator) {
            @Override
            public int getCountOfUnstableBars() {
                return 9;
            }

            @Override
            protected Num calculate(int index) {
                Num close = closeIndicator.getValue(index);
                Num high = highestHigh.getValue(index);
                Num low = lowestLow.getValue(index);
                Num diff = high.minus(low);
                // 若最高价=最低价，RSV设为50
                if (diff.isZero()) {
                    return numFactory.numOf(50);
                }
                return close.minus(low).dividedBy(diff).multipliedBy(numFactory.numOf(100));
            }
        };

        // 3.2 计算K值：EMA平滑（今日K = 2/3*昨日K + 1/3*今日RSV）
        Indicator<Num> k = new CachedIndicator<Num>(rsv) {
            @Override
            public int getCountOfUnstableBars() {
                return 9;
            }

            @Override
            protected Num calculate(int index) {
                if (index < rsvPeriod - 1) { // 前N-1个周期无有效值，设为50
                    return numFactory.numOf(50);
                }
                // 第一个有效K值 = RSV
                if (index == rsvPeriod - 1) {
                    return rsv.getValue(index);
                }
                // 后续K值：EMA平滑（权重1/3）
                Num prevK = getValue(index - 1);
                Num currentRSV = rsv.getValue(index);
                return prevK.multipliedBy(numFactory.numOf(2)).plus(currentRSV).dividedBy(numFactory.numOf(3));
            }
        };

        // 3.3 计算D值：EMA平滑（今日D = 2/3*昨日D + 1/3*今日K）
        Indicator<Num> d = new CachedIndicator<Num>(k) {
            @Override
            public int getCountOfUnstableBars() {
                return 9;
            }

            @Override
            protected Num calculate(int index) {
                if (index < rsvPeriod - 1) { // 前N-1个周期无有效值，设为50
                    return numFactory.numOf(50);
                }
                // 第一个有效D值 = K值
                if (index == rsvPeriod - 1) {
                    return k.getValue(index);
                }
                // 后续D值：EMA平滑（权重1/3）
                Num prevD = getValue(index - 1);
                Num currentK = k.getValue(index);
                return prevD.multipliedBy(numFactory.numOf(2)).plus(currentK).dividedBy(numFactory.numOf(3));
            }
        };

        // 3.4 计算J值：J = 3*K - 2*D
        Indicator<Num> j = new CachedIndicator<Num>(k) {
            @Override
            public int getCountOfUnstableBars() {
                return 9;
            }

            @Override
            protected Num calculate(int index) {
                Num kVal = k.getValue(index);
                Num dVal = d.getValue(index);
                return kVal.multipliedBy(numFactory.numOf(3)).minus(dVal.multipliedBy(numFactory.numOf(2)));
            }
        };

        // ==============================
        // 4. 取最后一根K线的KDJ值（和交易软件对比）
        // ==============================
        int todayIndex = series.getBarCount() - 1;
        // 确保索引有效（至少有rsvPeriod根K线）
        if (todayIndex < rsvPeriod - 1) {
            log.warn("K线数量不足，至少需要{}根", rsvPeriod);
            return null;
        }
        KdjContextResp kdjContextResp = new KdjContextResp();
        kdjContextResp.setBaseBarSeries(series);
        kdjContextResp.setK(k);
        kdjContextResp.setD(d);
        kdjContextResp.setJ(j);
        kdjContextResp.setRsvPeriod(rsvPeriod);
        return kdjContextResp;
    }

}
