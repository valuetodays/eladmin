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
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import ll.vt.quarkus.commons.msg.IVtNatsClient;
import lombok.extern.slf4j.Slf4j;
import me.vt.db.SqlServiceImpl;
import me.vt.modules.mybiz.api.dto.Cci14_100DataDto;
import me.vt.modules.mybiz.api.dto.StockDailyIndicatorDto;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorGetStockToBuyByKdjReq;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorGetStockToBuyByKdjResp;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.domain.StockDailyIndicator;
import me.vt.modules.mybiz.repository.StockDailyIndicatorRepository;
import me.vt.modules.mybiz.service.dto.Cci14_100DataCriteria;
import me.vt.modules.mybiz.service.dto.StockDailyIndicatorQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockDailyIndicatorConverter;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public String buildUpdateSqlForKdj(String code, LocalDate statDate, BigDecimal k, BigDecimal d, BigDecimal j) {
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
