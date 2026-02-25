package me.vt.modules.system.task;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import ll.vt.quarkus.commons.base.RunAsync;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author lei.liu
 * @since 2026-02-25
 */
@Slf4j
//@ApplicationScoped
public class DemoTask extends RunAsync {
    @Scheduled(cron = "*/20 * * * * ?")
    public void interval1() {
        super.executeAsync(() -> {
            log.info("interval1 begins");
            log.info("interval1 ends");
        });
    }
}
