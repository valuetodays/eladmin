package me.vt.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.mybiz.domain.StockDailyQuote;

import java.time.LocalDate;
import java.util.List;

/**
* @author valuetodays
* @since 2025-11-17 19:01
**/
@ApplicationScoped
public class StockDailyQuoteRepository extends MyPanacheRepository<StockDailyQuote> {

    public StockDailyQuote findByCodeAndStatDate(String code, LocalDate localDate) {
        return find("code = ?1 and statDate = ?2", code, localDate).firstResult();
    }
}
