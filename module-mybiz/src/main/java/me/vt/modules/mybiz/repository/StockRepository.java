package me.vt.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.mybiz.domain.Stock;

import java.util.List;

/**
* @author vt
* @since 2025-08-11 19:56
**/
@ApplicationScoped
public class StockRepository extends MyPanacheRepository<Stock> {
    public Stock findByCode(String code) {
        return find("code = ?1", code).firstResult();
    }

    public List<Stock> findAllByCodeIn(List<String> codes) {
        return find("code in ?1", codes).list();
    }
}
