package me.vt.modules.mybiz.service;

import cn.vt.exception.AssertUtils;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
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
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author valuetodays
 * @since 2025-11-18 20:01
 **/
@ApplicationScoped
public class StockDailyIndicatorServiceImpl {
    @Inject
    EntityManager entityManager;

    @Inject
    StockDailyIndicatorRepository stockDailyIndicatorRepository;
    @Inject
    StockDailyIndicatorMapper stockDailyIndicatorMapper;
    @Inject
    SqlServiceImpl sqlService;

    public PageResult<StockDailyIndicatorDto> queryAll(StockDailyIndicatorQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("statDate");
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
    public void upsert(String code, LocalDate statDate, BigDecimal cci14) {
        entityManager.createNativeQuery(
                """
                        INSERT INTO f_stock_daily_indicator (code, stat_date, cci14)
                        VALUES (:code, :statDate, :cci14)
                        ON CONFLICT (code, stat_date)
                        DO UPDATE SET
                            cci14 = EXCLUDED.cci14
                        """)
                .setParameter("code", code)
                .setParameter("statDate", statDate)
                .setParameter("cci14", cci14)
                .executeUpdate();
    }

    public List<Cci14_100DataDto> getAllCciLt_100ByStatDate(Cci14_100DataCriteria criteria) {
        LocalDate statDate = criteria.getStatDate();
        AssertUtils.assertNotNull(statDate);

        String sql = """
                SELECT sdi.code, sdi.stat_date as statDAte, sdi.cci14, ii.name
                FROM "f_stock_daily_indicator" sdi
                left join f_index_info ii on ii.code = sdi.code
                WHERE "stat_date"::date = ?1 and cci14 < -100
                ORDER BY "stat_date" DESC
                """;
        return sqlService.queryForList(sql, Cci14_100DataDto.class, statDate);
    }

}
