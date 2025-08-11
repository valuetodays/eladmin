package me.vt.modules.mybiz.repository;

import me.vt.modules.mybiz.domain.Stock;
import me.vt.MyPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
* @author vt
* @since 2025-08-11 19:56
**/
@ApplicationScoped
public class StockRepository extends MyPanacheRepository<Stock> {

}
