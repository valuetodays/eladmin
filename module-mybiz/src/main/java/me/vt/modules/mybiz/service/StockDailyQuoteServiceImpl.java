package me.vt.modules.mybiz.service;

import cn.vt.exception.AssertUtils;
import cn.vt.rest.third.utils.StockCodeUtils;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ll.vt.api2.module.fortune.client.util.PriceUtilsEx;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.StockDailyQuoteDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.repository.IndexInfoRepository;
import me.vt.modules.mybiz.repository.StockDailyQuoteRepository;
import me.vt.modules.mybiz.service.dto.StockDailyQuoteQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockDailyQuoteMapper;
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
public class StockDailyQuoteServiceImpl {

    @Inject
    StockDailyQuoteRepository stockDailyQuoteRepository;
    @Inject
    IndexInfoRepository indexInfoRepository;
    @Inject
    StockDailyQuoteMapper stockDailyQuoteMapper;
    @Inject
    StockDailyIndicatorServiceImpl stockDailyIndicatorService;

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
        List<StockDailyQuoteDto> list = stockDailyQuoteMapper.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<StockDailyQuoteDto> queryAll(StockDailyQuoteQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public StockDailyQuoteDto findById(Long id) {
        StockDailyQuote fStockDailyQuote = stockDailyQuoteRepository.findById(id);
        ValidationUtil.isNull(fStockDailyQuote.getId(), "FStockDailyQuote", "id", id);
        return stockDailyQuoteMapper.toDto(fStockDailyQuote);
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


    public void getAndSaveToDb(String code, boolean fully) {
        LocalDate today = LocalDate.now();

        int days;
        if (fully) {
            days = 500;
        } else {
            days = 30; // 近30天
        }
        LocalDate preDate = saveBatch(code, today, days);
        while (Objects.nonNull(preDate)) {
            preDate = saveBatch(code, preDate.minusDays(1), days);
            if (!fully) {
                break;
            }
        }

    }

    public static String formatCodeWithMarket(String rawCode) {
        String codeToUse;
        if (StringUtils.startsWith(rawCode, "9")) {
            codeToUse = rawCode + ".SZ";
        } else {
            codeToUse = StockCodeUtils.buildForEhaifangzhou(rawCode);
        }
        return codeToUse;
    }


    private LocalDate saveBatch(String code, LocalDate endDateInclude, int days) {
        LocalDate beginDate = endDateInclude.minusDays(days);
        String codeToUse = formatCodeWithMarket(code);
        log.info("processing record from {} to {} for code {}", beginDate, endDateInclude, codeToUse);
        List<DailyStatVo> dailyStats = HaitongApi.getDailyStats(codeToUse, DateUtils.formatAsYyyyMMdd(beginDate),
                DateUtils.formatAsYyyyMMdd(endDateInclude));
        if (CollectionUtils.isEmpty(dailyStats)) {
            return null;
        }
        for (DailyStatVo dailyStat : dailyStats) {
            String code1 = StockCodeUtils.parseFromEhaifangzhou(code);
            LocalDate localDate = DateUtils.formatYyyyMmDdAsLocalDateTime(dailyStat.getDate()).toLocalDate();
            StockDailyQuote queried = stockDailyQuoteRepository.findByCodeAndStatDate(code1, localDate);
            if (Objects.isNull(queried)) {
                queried = new StockDailyQuote();
                queried.setCode(code1);
                queried.setStatDate(localDate);
                queried.setOpenVal(PriceUtilsEx.fixPrice(dailyStat.getOpen()));
                queried.setCloseVal(PriceUtilsEx.fixPrice(dailyStat.getClose()));
                queried.setHighVal(PriceUtilsEx.fixPrice(dailyStat.getHigh()));
                queried.setLowVal(PriceUtilsEx.fixPrice(dailyStat.getLow()));
                queried.setVolumeVal(dailyStat.getVolume());
                queried.setAmountVal(dailyStat.getAmount());
                try {
                    stockDailyQuoteRepository.save(queried);
                } catch (Exception e) {
                    log.error("save error", e);
                }
            }
        }
        return beginDate;
    }

    @Transactional
    public Long computeAllCciById(IndexInfo req) {
        Long indexInfoId = req.getId();
        IndexInfo old = indexInfoRepository.findById(indexInfoId);
        AssertUtils.assertNotNull(old);
        // 要异步
        this.computeCci(old.getCode(), true);
        // 要通知
        // 要处理重复点击问题
        return 1L;
    }

    @Transactional
    public void computeLatest30DaysCci(String indexCode) {
        this.computeCci(indexCode, false);
    }

    private void computeCci(String indexCode, boolean fully) {
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
        for (int n = bars.size() - 1; n >= cci14.getCountOfUnstableBars() - 1; n--) {
            Num value = cci14.getValue(n);
            log.info("#n={}, date={} value={}", n, bars.get(n).getSystemZonedEndTime(), value);
            LocalDate statDate = bars.get(n).getSystemZonedEndTime().toLocalDate();
            BigDecimal cci14BD = PriceUtilsEx.fixPrice(BigDecimal.valueOf(value.getDelegate().doubleValue()));
            try {
                stockDailyIndicatorService.upsert(indexCode, statDate, cci14BD);
            } catch (Exception e) {
                log.error("error when upsert", e);
            }
        }
    }

}
