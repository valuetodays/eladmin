package me.vt.modules.system.task;

import com.vt.quarkus.commons.base.RunAsync;
import com.vt.quarkus.commons.msg.IVtNatsClient;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.rest.StockDailyIndicatorController;
import me.vt.modules.mybiz.rest.StockDailyQuoteController;
import me.vt.modules.mybiz.rest.StockInfoController;
import me.vt.modules.mybiz.service.dto.Cci14_100DataCriteria;

import java.time.LocalDate;

@Slf4j
@ApplicationScoped
public class StockInfoDailyTask extends RunAsync {

    @Inject
    StockInfoController stockInfoController;
    @Inject
    StockDailyQuoteController stockDailyQuoteController;
    @Inject
    StockDailyIndicatorController stockDailyIndicatorController;
    @Inject
    IVtNatsClient vtNatsClient;

    // 每天15:00:35
    @Scheduled(cron = "35 0 15 * * ?")
    public void saveLatest30Days() {
        super.executeAsync(() -> {
            try {
                stockInfoController.saveLatest30Days();
            } catch (Exception e) {
                vtNatsClient.publishApplicationException("saveLatest30Days", e);
            }
        });
    }

    @Scheduled(cron = "35 2 15 * * ?")
    public void computeLatest30DaysCci() {
        super.executeAsync(() -> {
            try {
                stockDailyQuoteController.computeLatest30DaysCci();
            } catch (Exception e) {
                vtNatsClient.publishApplicationException("computeLatest30DaysCci", e);
            }
        });
    }

    @Scheduled(cron = "35 4 15 * * ?")
    public void getAllCciLt_100ByStatDate() {
        super.executeAsync(() -> {
            Cci14_100DataCriteria c = new Cci14_100DataCriteria();
            c.setStatDate(LocalDate.now());
            c.setPushMsg(true);
            try {
                stockDailyIndicatorController.getAllCciLt_100ByStatDate(c);
            } catch (Exception e) {
                vtNatsClient.publishApplicationException("getAllCciLt_100ByStatDate", e);
            }
        });
    }

}
