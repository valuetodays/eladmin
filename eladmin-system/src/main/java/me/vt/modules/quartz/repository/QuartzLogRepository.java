package me.vt.modules.quartz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.quartz.domain.QuartzLog;

/**
 * @author Zheng Jie
 * @since 2019-01-07
 */
@ApplicationScoped
public class QuartzLogRepository extends MyPanacheRepository<QuartzLog> {

}
