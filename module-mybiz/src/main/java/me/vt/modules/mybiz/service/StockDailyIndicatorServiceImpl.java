package me.vt.modules.mybiz.service;

import cn.vt.exception.AssertUtils;
import cn.vt.util.DateUtils;
import com.p6spy.engine.common.P6Util;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ll.vt.api2.module.fortune.client.util.PriceUtilsEx;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import ll.vt.quarkus.commons.msg.IVtNatsClient;
import lombok.extern.slf4j.Slf4j;
import me.vt.db.SqlServiceImpl;
import me.vt.modules.mybiz.api.dto.Cci14_100DataDto;
import me.vt.modules.mybiz.api.dto.StockDailyIndicatorDto;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorGetStockToBuyByKdjReq;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorGetStockToBuyByKdjResp;
import me.vt.modules.mybiz.domain.StockDailyIndicator;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.repository.StockDailyIndicatorRepository;
import me.vt.modules.mybiz.repository.StockDailyQuoteRepository;
import me.vt.modules.mybiz.service.dto.Cci14_100DataCriteria;
import me.vt.modules.mybiz.service.dto.StockDailyIndicatorQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockDailyIndicatorConverter;
import me.vt.modules.mybiz.service.ta4j.CciIndicatorHelper;
import me.vt.modules.mybiz.service.ta4j.KdjIndicatorHelper;
import me.vt.modules.mybiz.service.ta4j.Ta4jUtils;
import me.vt.modules.mybiz.service.ta4j.pojo.KdjContextResp;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.tuple.Pair;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.bars.TimeBarBuilderFactory;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.indicators.averages.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.num.DecimalNumFactory;
import org.ta4j.core.num.Num;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author valuetodays
 * @since 2025-11-18 20:01
 **/
@Slf4j
@ApplicationScoped
public class StockDailyIndicatorServiceImpl {

    @Inject
    StockDailyIndicatorRepository stockDailyIndicatorRepository;
    @Inject
    StockDailyIndicatorConverter stockDailyIndicatorConverter;
    @Inject
    SqlServiceImpl sqlService;
    @Inject
    IVtNatsClient vtNatsClient;
    @Inject
    StockDailyQuoteRepository stockDailyQuoteRepository;

    public PageResult<StockDailyIndicatorDto> queryAll(StockDailyIndicatorQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id.statDate");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, StockDailyIndicator.class);
        PanacheQuery<StockDailyIndicator> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = stockDailyIndicatorRepository.findAll(sort);
        } else {
            panacheQuery = stockDailyIndicatorRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }

        PanacheQuery<StockDailyIndicator> all = panacheQuery.page(pageable);
        List<StockDailyIndicatorDto> list = stockDailyIndicatorConverter.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<StockDailyIndicatorDto> queryAll(StockDailyIndicatorQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }


    @Transactional(rollbackOn = Exception.class)
    public void create(StockDailyIndicator resources) {
        stockDailyIndicatorRepository.save(resources);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        stockDailyIndicatorRepository.deleteAllByIdIn(ids);
    }




    public void computeCci(String code, boolean fully) throws SQLException {
        List<StockDailyQuote> stockDailyQuotes;
        if (fully) {
            stockDailyQuotes = stockDailyQuoteRepository.findAllByCodeOrderByStatDateDesc(code);
        } else {
            stockDailyQuotes = stockDailyQuoteRepository.findTop60ByCodeOrderByStatDateDesc(code);
        }
        if (CollectionUtils.isEmpty(stockDailyQuotes)) {
            return;
        }
        // 需要使用“正序”来计算cci
        List<Bar> bars = stockDailyQuotes.stream()
            .sorted(Comparator.comparing(StockDailyQuote::getStatDate))
            .map(e -> Ta4jUtils.buildBar(
                e.getStatDate(),
                e.getOpenVal(), e.getCloseVal(),
                e.getHighVal(), e.getLowVal(),
                e.getVolumeVal(), e.getAmountVal(),
                0))
            .toList();
        CCIIndicator cci14 = CciIndicatorHelper.buildCci(bars, code, null);

        final int SIZE = fully ? 500 : 30;
        List<String> sqlsToExecute = new ArrayList<>(SIZE);
        for (int n = bars.size() - 1; n >= cci14.getCountOfUnstableBars() - 1; n--) {
            Num value = cci14.getValue(n);
//            log.info("#n={}, date={} value={}", n, bars.get(n).getSystemZonedEndTime(), value);
            LocalDate statDate = bars.get(n).getSystemZonedEndTime().toLocalDate();
            BigDecimal cci14BD = PriceUtilsEx.fixPrice(BigDecimal.valueOf(value.getDelegate().doubleValue()));
            String sql = this.buildUpsertSqlForCCi(code, statDate, cci14BD);
            sqlsToExecute.add(sql);
            if (sqlsToExecute.size() >= SIZE) {
                sqlService.saveBySqls(sqlsToExecute);
                sqlsToExecute.clear();
                // 不是更新全部，就只更新30条
                if (!fully) {
                    return;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(sqlsToExecute)) {
            sqlService.saveBySqls(sqlsToExecute);
        }
    }

    @Transactional
    public String buildUpsertSqlForCCi(String code, LocalDate statDate, BigDecimal cci14) {
        final String SQL_FOR_UPSERT_CCI = """
                insert into f_stock_daily_indicator (code, stat_date, cci14) values('?code', '?stat_date', ?cci14)
                ON CONFLICT (code, stat_date)
                DO UPDATE SET
                    cci14   = EXCLUDED.cci14;
            """;

        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("stat_date", statDate.format(DateUtils.DEFAULT_DATE_FORMATTER));
        params.put("cci14", String.valueOf(cci14));
        String sql = P6Util.singleLine(SQL_FOR_UPSERT_CCI);
        for (Map.Entry<String, String> stringObjectEntry : params.entrySet()) {
            sql = StringUtils.replace(sql, "?" + stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return sql;
    }

    public void computeKdj(String code, boolean fully) throws SQLException {
        // 1. 获取日K数据
        List<StockDailyQuote> stockDailyQuotes;
        if (fully) {
            stockDailyQuotes = stockDailyQuoteRepository.findAllByCodeOrderByStatDateDesc(code);
        } else {
            stockDailyQuotes = stockDailyQuoteRepository.findTop60ByCodeOrderByStatDateDesc(code);
        }
        if (CollectionUtils.isEmpty(stockDailyQuotes)) {
            return;
        }
        final int rsvPeriod = 9;
        KdjContextResp kdjContextResp = KdjIndicatorHelper.computeKdj(stockDailyQuotes, rsvPeriod);
        if (Objects.isNull(kdjContextResp)) {
            return;
        }
        BarSeries baseBarSeries = kdjContextResp.getBaseBarSeries();
        Indicator<Num> k = kdjContextResp.getK();
        Indicator<Num> d = kdjContextResp.getD();
        Indicator<Num> j = kdjContextResp.getJ();

        final int SIZE = fully ? 500 : 30;
        List<String> sqlsToExecute = new ArrayList<>(SIZE);
        for (int i = baseBarSeries.getBarCount() - 1; i >= 0; i--) {
            Bar bar = baseBarSeries.getBar(i);
            LocalDate statDate = bar.getSystemZonedEndTime().toLocalDate();

            BigDecimal kValue = k.getValue(i).bigDecimalValue();
            BigDecimal dValue = d.getValue(i).bigDecimalValue();
            BigDecimal jValue = j.getValue(i).bigDecimalValue();

            // 格式化输出：前8天标注“无有效值”，后续保留3位小数
            if (i < rsvPeriod - 1) {
                log.info("{}\t\t无有效值\t无有效值\t无有效值", statDate);
                continue;
            }
//            log.info(String.format("%s\t\t%.3f\t\t%.3f\t\t%.3f", statDate, kValue, dValue, jValue));
            String updateSqlForKdj = this.buildUpdateSqlForKdj(code, statDate, kValue, dValue, jValue);
            log.info("updateSqlForKdj={}", updateSqlForKdj);
            sqlsToExecute.add(updateSqlForKdj);
            if (sqlsToExecute.size() >= SIZE) {
                sqlService.saveBySqls(sqlsToExecute);
                sqlsToExecute.clear();
                // 不是更新全部，就只更新30条
                if (!fully) {
                    return;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(sqlsToExecute)) {
            sqlService.saveBySqls(sqlsToExecute);
        }
    }
    private String buildUpdateSqlForKdj(String code, LocalDate statDate, BigDecimal k, BigDecimal d, BigDecimal j) {
        final String SQL_FOR_UPDATE_KDJ = """
            update f_stock_daily_indicator
            set kdj_k = ?kdj_k, kdj_d = ?kdj_d, kdj_j = ?kdj_j
            where code = '?code' and stat_date = '?stat_date';
        """;
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("stat_date", statDate.format(DateUtils.DEFAULT_DATE_FORMATTER));
        params.put("kdj_k", String.valueOf(k));
        params.put("kdj_d", String.valueOf(d));
        params.put("kdj_j", String.valueOf(j));
        String sql = P6Util.singleLine(SQL_FOR_UPDATE_KDJ);
        for (Map.Entry<String, String> stringObjectEntry : params.entrySet()) {
            sql = Strings.CS.replace(sql, "?" + stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return sql;
    }

    public void computeMa(String code, boolean fully) throws SQLException {
        List<StockDailyQuote> stockDailyQuotes;
        if (fully) {
            stockDailyQuotes = stockDailyQuoteRepository.findAllByCodeOrderByStatDateDesc(code);
        } else {
            stockDailyQuotes = stockDailyQuoteRepository.findTop60ByCodeOrderByStatDateDesc(code);
        }
        if (CollectionUtils.isEmpty(stockDailyQuotes)) {
            return;
        }
        // 需要使用“正序”
        List<Bar> bars = stockDailyQuotes.stream()
            .sorted(Comparator.comparing(StockDailyQuote::getStatDate))
            .map(e -> Ta4jUtils.buildBar(
                e.getStatDate(),
                e.getOpenVal(), e.getCloseVal(),
                e.getHighVal(), e.getLowVal(),
                e.getVolumeVal(), e.getAmountVal(),
                0))
            .toList();
        BaseBarSeriesBuilder baseBarSeriesBuilder = new BaseBarSeriesBuilder();
        baseBarSeriesBuilder.withName(code)
            .withBars(bars)
            .withNumFactory(DecimalNumFactory.getInstance())
            .withBarBuilderFactory(new TimeBarBuilderFactory());
        BaseBarSeries baseBarSeries = baseBarSeriesBuilder.build();
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(baseBarSeries);
        SMAIndicator ma5Indicator = new SMAIndicator(closePriceIndicator, 5);
        SMAIndicator ma20Indicator = new SMAIndicator(closePriceIndicator, 20);
        SMAIndicator ma30Indicator = new SMAIndicator(closePriceIndicator, 30);
        SMAIndicator ma60Indicator = new SMAIndicator(closePriceIndicator, 60);
        SMAIndicator ma120Indicator = new SMAIndicator(closePriceIndicator, 120);
        SMAIndicator ma240Indicator = new SMAIndicator(closePriceIndicator, 240);

        VolumeIndicator volumeIndicator = new VolumeIndicator(baseBarSeries);
        SMAIndicator volMa5Indicator = new SMAIndicator(volumeIndicator, 5);
        SMAIndicator volMa20Indicator = new SMAIndicator(volumeIndicator, 20);

        final int SIZE = fully ? 500 : 30;

        List<String> sqlsToExecute = new ArrayList<>(SIZE);
        for (int n = bars.size() - 1; n >= 0; n--) {
            LocalDate statDate = bars.get(n).getSystemZonedEndTime().toLocalDate();
            Num ma5Val = ma5Indicator.getValue(n);
            Num ma20Val = ma20Indicator.getValue(n);
            Num ma30Val = ma30Indicator.getValue(n);
            Num ma60Val = ma60Indicator.getValue(n);
            Num ma120Val = ma120Indicator.getValue(n);
            Num ma240Val = ma240Indicator.getValue(n);

            Num ma5VolVal = volMa5Indicator.getValue(n);
            Num ma20VolVal = volMa20Indicator.getValue(n);

            log.info("#n={}, date={} ma5Val={}, ma20Val={}, ma30Val={}, ma60Val={}, ma120Val={}, ma240Val={}, ma5VolVal={}, ma20VolVal={}",
                n, statDate, ma5Val, ma20Val, ma30Val, ma60Val, ma120Val, ma240Val, ma5VolVal, ma20VolVal);

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("stat_date", bars.get(n).getSystemZonedEndTime().format(DateUtils.DEFAULT_DATE_FORMATTER));
            params.put("ma5", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma5Val.getDelegate().doubleValue())));
            params.put("ma20", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma20Val.getDelegate().doubleValue())));
            params.put("ma30", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma30Val.getDelegate().doubleValue())));
            params.put("ma60", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma60Val.getDelegate().doubleValue())));
            params.put("ma120", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma120Val.getDelegate().doubleValue())));
            params.put("ma240", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma240Val.getDelegate().doubleValue())));
            params.put("ma5Volume", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma5VolVal.getDelegate().doubleValue())));
            params.put("ma20Volume", PriceUtilsEx.fixPrice(BigDecimal.valueOf(ma20VolVal.getDelegate().doubleValue())));

            sqlsToExecute.add(buildUpdateSqlForMa(params));
            if (sqlsToExecute.size() >= SIZE) {
                sqlService.saveBySqls(sqlsToExecute);
                sqlsToExecute.clear();
                // 不是更新全部，就只更新30条
                if (!fully) {
                    return;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(sqlsToExecute)) {
            sqlService.saveBySqls(sqlsToExecute);
        }
    }

    private String buildUpdateSqlForMa(Map<String, Object> params) {
        String sql = """
            update f_stock_daily_indicator
            set ma5 = ?ma5?, ma20 = ?ma20?, ma30 = ?ma30?, ma60=?ma60?, ma120=?ma120?, ma240=?ma240?,
            ma5_volume=?ma5Volume?, ma20_volume=?ma20Volume?
            where code = '?code?' and stat_date = '?stat_date?';
        """;

        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            sql = StringUtils.replace(sql, "?" + stringObjectEntry.getKey() + "?", String.valueOf(stringObjectEntry.getValue()));
        }
        return sql;
    }

    public List<Cci14_100DataDto> getAllCciLt_100ByStatDate(Cci14_100DataCriteria criteria) {
        LocalDate statDate = criteria.getStatDate();
        AssertUtils.assertNotNull(statDate);

        final String sql = """
                SELECT sdi.code, sdi.stat_date as statDate, sdi.cci14, ii.short_name as name
                FROM f_stock_daily_indicator sdi
                left join f_stock_info ii on ii.code = sdi.code
                WHERE sdi.stat_date::date = :statDate::date and cci14 < -100
                ORDER BY code DESC
                """;
        List<Cci14_100DataDto> list = sqlService.getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind("statDate", statDate)
                .mapToBean(Cci14_100DataDto.class)
                .list());
        boolean pushMsg = criteria.isPushMsg();
        if (pushMsg) {
            String msg = StringUtils.joinWith("\n", list);
            vtNatsClient.publishApplicationMessage(msg);
        }
        return list;
    }

    public List<StockDailyIndicatorGetStockToBuyByKdjResp> getStocksToBuyByKdj(StockDailyIndicatorGetStockToBuyByKdjReq req) {
        LocalDate statDate = req.getStatDate();

        final String sql = """
            SELECT sdi.code, sdi.stat_date as statDate, sdi.kdj_k as k, sdi.kdj_d as d, sdi.kdj_j as j, ii.short_name as name
            FROM f_stock_daily_indicator sdi
            left join f_stock_info ii on ii.code = sdi.code
            WHERE sdi.stat_date::date = :statDate::date
                and (sdi.kdj_k < 20 and sdi.kdj_d < 20 and sdi.kdj_j < 20)
            ORDER BY code DESC
            """;
        List<StockDailyIndicatorGetStockToBuyByKdjResp> list = sqlService.getJdbi().withHandle(handle -> handle.createQuery(sql)
            .bind("statDate", statDate)
            .mapToBean(StockDailyIndicatorGetStockToBuyByKdjResp.class)
            .list());
        boolean pushMsg = req.isPushMsg();
        if (pushMsg) {
            String msg = StringUtils.joinWith("\n", list);
            vtNatsClient.publishApplicationMessage(msg);
        }
        return list;
    }
}
