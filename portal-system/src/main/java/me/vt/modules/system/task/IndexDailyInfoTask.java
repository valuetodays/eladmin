package me.vt.modules.system.task;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ll.vt.quarkus.commons.base.RunAsync;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.rest.IndexInfoController;
import me.vt.modules.mybiz.rest.StockDailyIndicatorController;
import me.vt.modules.mybiz.rest.StockDailyQuoteController;
import me.vt.modules.mybiz.service.dto.Cci14_100DataCriteria;

import java.time.LocalDate;

@Slf4j
@ApplicationScoped
public class IndexDailyInfoTask extends RunAsync {

    @Inject
    IndexInfoController indexInfoController;
    @Inject
    StockDailyQuoteController stockDailyQuoteController;
    @Inject
    StockDailyIndicatorController stockDailyIndicatorController;

    // 每天15:00:35
    @Scheduled(cron = "35 0 15 * * ?")
    public void saveLatest30Days() {
        super.executeAsync(() -> {
            indexInfoController.saveLatest30Days();
        });
    }

    @Scheduled(cron = "35 2 15 * * ?")
    public void computeLatest30DaysCci() {
        super.executeAsync(() -> {
            stockDailyQuoteController.computeLatest30DaysCci();
        });
    }

    @Scheduled(cron = "35 4 15 * * ?")
    public void getAllCciLt_100ByStatDate() {
        super.executeAsync(() -> {
            Cci14_100DataCriteria c = new Cci14_100DataCriteria();
            c.setStatDate(LocalDate.now());
            c.setPushMsg(true);
            stockDailyIndicatorController.getAllCciLt_100ByStatDate(c);
        });
    }

}
