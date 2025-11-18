package me.vt.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.mybiz.domain.StockDailyIndicator;

/**
* @author valuetodays
* @since 2025-11-18 20:01
**/
@ApplicationScoped
public class StockDailyIndicatorRepository extends MyPanacheRepository<StockDailyIndicator> {

}
