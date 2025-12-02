package me.vt.modules.mybiz.service.ta4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.DecimalNum;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-18
 */
public class Ta4jUtils {
    public static Bar buildBar(LocalDate date,
            BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low,
            BigDecimal volume, BigDecimal amount,
            long count) {
        return new BaseBar(
                java.time.Duration.ofDays(1), // 日K线（周期1天）
                date.atStartOfDay().toInstant(ZoneOffset.ofHours(8)), // 开始时间
                DecimalNum.valueOf(open), // 开盘价
                DecimalNum.valueOf(high), // 最高价
                DecimalNum.valueOf(low), // 最低价
                DecimalNum.valueOf(close), // 收盘价
                DecimalNum.valueOf(volume), // 成交量（无数据可填0）
                DecimalNum.valueOf(amount), // 成交额
                count);
    }
}
