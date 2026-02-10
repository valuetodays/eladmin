package me.vt.modules.mybiz.service;

import cn.vt.exception.AssertUtils;
import cn.vt.util.DateUtils;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import ll.vt.quarkus.commons.msg.IVtNatsClient;
import me.vt.db.SqlServiceImpl;
import me.vt.modules.mybiz.api.dto.Cci14_100DataDto;
import me.vt.modules.mybiz.api.dto.StockDailyIndicatorDto;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.domain.StockDailyIndicator;
import me.vt.modules.mybiz.repository.StockDailyIndicatorRepository;
import me.vt.modules.mybiz.service.dto.Cci14_100DataCriteria;
import me.vt.modules.mybiz.service.dto.StockDailyIndicatorQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockDailyIndicatorMapper;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author valuetodays
 * @since 2025-11-18 20:01
 **/
@ApplicationScoped
public class StockDailyIndicatorServiceImpl {

    @Inject
    StockDailyIndicatorRepository stockDailyIndicatorRepository;
    @Inject
    StockDailyIndicatorMapper stockDailyIndicatorMapper;
    @Inject
    SqlServiceImpl sqlService;
    @Inject
    IVtNatsClient vtNatsClient;

    private static final String SQL_FOR_UPSERT = """
                insert into f_stock_daily_indicator (code, stat_date, cci14) values('?code', '?stat_date', ?cci14)
                ON CONFLICT (code, stat_date)
                DO UPDATE SET
                    cci14   = EXCLUDED.cci14;
            """;

    public PageResult<StockDailyIndicatorDto> queryAll(StockDailyIndicatorQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id.statDate");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, Stock.class);
        PanacheQuery<StockDailyIndicator> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = stockDailyIndicatorRepository.findAll(sort);
        } else {
            panacheQuery = stockDailyIndicatorRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }

        PanacheQuery<StockDailyIndicator> all = panacheQuery.page(pageable);
        List<StockDailyIndicatorDto> list = stockDailyIndicatorMapper.toDto(all.list());
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

    @Transactional
    public String buildUpsertSql(String code, LocalDate statDate, BigDecimal cci14) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("stat_date", statDate.format(DateUtils.DEFAULT_DATE_FORMATTER));
        params.put("cci14", String.valueOf(cci14));
        String sql = SQL_FOR_UPSERT;
        for (Map.Entry<String, String> stringObjectEntry : params.entrySet()) {
            sql = StringUtils.replace(sql, "?" + stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return sql;
    }

    public List<Cci14_100DataDto> getAllCciLt_100ByStatDate(Cci14_100DataCriteria criteria) {
        LocalDate statDate = criteria.getStatDate();
        AssertUtils.assertNotNull(statDate);

        final String sql = """
                SELECT sdi.code, sdi.stat_date as statDate, sdi.cci14, ii.name
                FROM f_stock_daily_indicator sdi
                left join f_index_info ii on ii.code = sdi.code
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

}
