package me.vt.modules.mybiz.service;
import java.math.BigDecimal;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.repository.StockDailyQuoteRepository;
import me.vt.modules.mybiz.service.dto.StockDailyQuoteQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockDailyQuoteMapper;
import me.vt.utils.FileUtil;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import me.vt.utils.ValidationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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
    StockDailyQuoteMapper stockDailyQuoteMapper;

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

    private LocalDate saveBatch(String code, LocalDate endDateInclude, int days) {
        LocalDate beginDate = endDateInclude.minusDays(days);
        log.info("processing record from {} to {} for code {}", beginDate, endDateInclude, code);
        List<DailyStatVo> dailyStats = HaitongApi.getDailyStats(code, DateUtils.formatAsYyyyMMdd(beginDate), DateUtils.formatAsYyyyMMdd(endDateInclude));
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
}
