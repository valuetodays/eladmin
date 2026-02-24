package me.vt.modules.mybiz.service;

import cn.vt.exception.AssertUtils;
import cn.vt.trade.api.HaitongApi;
import cn.vt.trade.vo.DailyStatVo;
import cn.vt.util.DateUtils;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ll.vt.api2.module.fortune.client.util.PriceUtilsEx;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import ll.vt.quarkus.commons.base.RunAsync;
import ll.vt.quarkus.commons.msg.IVtNatsClient;
import lombok.extern.slf4j.Slf4j;
import me.vt.db.SqlServiceImpl;
import me.vt.modules.mybiz.api.dto.StockDailyQuoteDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.domain.StockInfoPersist;
import me.vt.modules.mybiz.repository.StockDailyQuoteRepository;
import me.vt.modules.mybiz.repository.StockInfoRepository;
import me.vt.modules.mybiz.service.dto.StockDailyQuoteQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockDailyQuoteConverter;
import me.vt.modules.mybiz.service.ta4j.Ta4jUtils;
import me.vt.utils.FileUtil;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import me.vt.utils.ValidationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.bars.TimeBarBuilderFactory;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.num.DecimalNumFactory;
import org.ta4j.core.num.Num;

/**
 * @author valuetodays
 * @since 2025-11-17 19:01
 **/
@ApplicationScoped
@Slf4j
public class StockDailyQuoteServiceImpl extends RunAsync {

    @Inject
    StockDailyQuoteRepository stockDailyQuoteRepository;
    @Inject
    StockInfoRepository stockInfoRepository;
    @Inject
    StockDailyQuoteConverter stockDailyQuoteConverter;
    @Inject
    StockDailyIndicatorServiceImpl stockDailyIndicatorService;
    @Inject
    SqlServiceImpl sqlService;
    @Inject
    IVtNatsClient vtNatsClient;

    private static final String sqlUpsertTpl =
            """
                    insert into f_stock_daily_quote(code, stat_date, open_val, close_val, high_val, low_val, volume_val, amount_val)
                    values('?code', '?stat_date', ?open_val, ?close_val, ?high_val, ?low_val, ?volume_val, ?amount_val)
                    ON CONFLICT (code, stat_date)
                    DO UPDATE SET
                        open_val   = EXCLUDED.open_val,
                        close_val  = EXCLUDED.close_val,
                        high_val   = EXCLUDED.high_val,
                        low_val    = EXCLUDED.low_val,
                        volume_val = EXCLUDED.volume_val,
                        amount_val = EXCLUDED.amount_val;
                    """;


    public PageResult<StockDailyQuoteDto> queryAll(StockDailyQuoteQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, Stock.class);
        PanacheQuery<StockDailyQuote> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = stockDailyQuoteRepository.findAll(sort);
        } else {
            panacheQuery = stockDailyQuoteRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }

        PanacheQuery<StockDailyQuote> all = panacheQuery.page(pageable);
        List<StockDailyQuoteDto> list = stockDailyQuoteConverter.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<StockDailyQuoteDto> queryAll(StockDailyQuoteQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public StockDailyQuoteDto findById(Long id) {
        StockDailyQuote fStockDailyQuote = stockDailyQuoteRepository.findById(id);
        ValidationUtil.isNull(fStockDailyQuote.getId(), "FStockDailyQuote", "id", id);
        return stockDailyQuoteConverter.toDto(fStockDailyQuote);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(StockDailyQuote resources) {
        stockDailyQuoteRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(StockDailyQuote resources) {
        StockDailyQuote fStockDailyQuote = stockDailyQuoteRepository.findById(resources.getId());
        ValidationUtil.isNull(fStockDailyQuote.getId(), "FStockDailyQuote", "id", resources.getId());
        fStockDailyQuote.copy(resources);
        stockDailyQuoteRepository.save(fStockDailyQuote);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            stockDailyQuoteRepository.deleteById(id);
        }
    }

    public File download(List<StockDailyQuoteDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockDailyQuoteDto fStockDailyQuote : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("编号", fStockDailyQuote.getCode());
            map.put("统计日期", fStockDailyQuote.getStatDate());
            map.put("开盘点数", fStockDailyQuote.getOpenVal());
            map.put("收盘点数", fStockDailyQuote.getCloseVal());
            map.put("最高点数", fStockDailyQuote.getHighVal());
            map.put("最低点数", fStockDailyQuote.getLowVal());
            map.put("成交量", fStockDailyQuote.getVolumeVal());
            map.put("成交额", fStockDailyQuote.getAmountVal());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }


    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void getAndSaveToDb(StockInfoPersist indexInfo, boolean fully) {
        LocalDate today = LocalDate.now();

        int days;
        if (fully) {
            days = 500;
        } else {
            days = 30; // 近30天
        }
        try {
            LocalDate preDate = saveBatch(indexInfo, today, days);
            while (Objects.nonNull(preDate)) {
                preDate = saveBatch(indexInfo, preDate.minusDays(1), days);
                if (!fully) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("error when getAndSaveToDb()", e);
        }
    }

    private LocalDate saveBatch(StockInfoPersist indexInfo, LocalDate endDateInclude, int days) throws SQLException {
        LocalDate beginDate = endDateInclude.minusDays(days);
        String codeToUse = indexInfo.getCode() + "." + indexInfo.getRegion();
        log.info("processing record from {} to {} for code {}", beginDate, endDateInclude, codeToUse);
        List<DailyStatVo> dailyStats = HaitongApi.getDailyStats(codeToUse, DateUtils.formatAsYyyyMMdd(beginDate),
                DateUtils.formatAsYyyyMMdd(endDateInclude));
        if (CollectionUtils.isEmpty(dailyStats)) {
            return null;
        }
        List<String> sqlsToExecute = new ArrayList<>(dailyStats.size());
        for (DailyStatVo dailyStat : dailyStats) {
            Map<String, Object> params = new HashMap<>();
            params.put("code", indexInfo.getCode());
            params.put("stat_date", dateIntToDateStr(dailyStat.getDate()));
            params.put("open_val", PriceUtilsEx.fixPrice(dailyStat.getOpen()));
            params.put("close_val", PriceUtilsEx.fixPrice(dailyStat.getClose()));
            params.put("high_val", PriceUtilsEx.fixPrice(dailyStat.getHigh()));
            params.put("low_val", PriceUtilsEx.fixPrice(dailyStat.getLow()));
            params.put("volume_val", dailyStat.getVolume());
            params.put("amount_val", dailyStat.getAmount());
            String sql = sqlUpsertTpl;
            for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
                sql = StringUtils.replace(sql, "?" + stringObjectEntry.getKey(),
                        String.valueOf(stringObjectEntry.getValue()));
            }
            sqlsToExecute.add(sql);
        }
        sqlService.saveBySqls(sqlsToExecute);
        return beginDate;
    }

    protected String dateIntToDateStr(int date) {
        LocalDateTime t = DateUtils.formatYyyyMmDdAsLocalDateTime(date);
        return DateUtils.formatDate(t);
    }

    @Transactional
    public Long computeAllCciById(IndexInfo req) throws SQLException {
        Long stockInfoId = req.getId();
        StockInfoPersist stockInfoPersist = stockInfoRepository.findById(stockInfoId);
        AssertUtils.assertNotNull(stockInfoPersist);
        // 要异步
        this.computeCci(stockInfoPersist.getCode(), true);
        // 要通知
        // 要处理重复点击问题
        return 1L;
    }

    @Transactional
    public void computeLatest30DaysCci(String indexCode) throws SQLException {
        this.computeCci(indexCode, false);
    }

    private void computeCci(String indexCode, boolean fully) throws SQLException {
        List<StockDailyQuote> stockDailyQuotes;
        if (fully) {
            stockDailyQuotes = stockDailyQuoteRepository.findAllByCodeOrderByStatDateDesc(indexCode);
        } else {
            stockDailyQuotes = stockDailyQuoteRepository.findTop60ByCodeOrderByStatDateDesc(indexCode);
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
        BaseBarSeriesBuilder baseBarSeriesBuilder = new BaseBarSeriesBuilder();
        baseBarSeriesBuilder.withName(indexCode)
                .withBars(bars)
                .withNumFactory(DecimalNumFactory.getInstance(3))
                .withBarBuilderFactory(new TimeBarBuilderFactory());
        BaseBarSeries baseBarSeries = baseBarSeriesBuilder.build();
        CCIIndicator cci14 = new CCIIndicator(
                baseBarSeries, // 基于TP计算CCI
                14 // 周期N=14
        );
        final int SIZE = fully ? 500 : 30;
        List<String> sqlsToExecute = new ArrayList<>(SIZE);
        for (int n = bars.size() - 1; n >= cci14.getCountOfUnstableBars() - 1; n--) {
            Num value = cci14.getValue(n);
            log.info("#n={}, date={} value={}", n, bars.get(n).getSystemZonedEndTime(), value);
            LocalDate statDate = bars.get(n).getSystemZonedEndTime().toLocalDate();
            BigDecimal cci14BD = PriceUtilsEx.fixPrice(BigDecimal.valueOf(value.getDelegate().doubleValue()));
            String sql = stockDailyIndicatorService.buildUpsertSql(indexCode, statDate, cci14BD);
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

}
