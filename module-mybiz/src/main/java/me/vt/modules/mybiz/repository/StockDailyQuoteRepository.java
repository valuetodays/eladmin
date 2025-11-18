package me.vt.modules.mybiz.repository;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.mybiz.domain.StockDailyQuote;

/**
* @author valuetodays
* @since 2025-11-17 19:01
**/
@ApplicationScoped
public class StockDailyQuoteRepository extends MyPanacheRepository<StockDailyQuote> {

    public StockDailyQuote findByCodeAndStatDate(String code, LocalDate localDate) {
        return find("code = ?1 and statDate = ?2", code, localDate).firstResult();
    }

    public List<StockDailyQuote> findAllByCodeOrderByStatDateDesc(String code) {
        return find("code = ?1", Sort.descending("statDate"), code).list();
    }

    /**
     * 计算cci14时，需要前13天的数据，所以查数据时多查一定天数的记录.
     */
    public List<StockDailyQuote> findTop60ByCodeOrderByStatDateDesc(String indexCode) {
        return find("code = ?1", Sort.descending("statDate"), indexCode).page(Page.ofSize(60)).list();
    }
}
