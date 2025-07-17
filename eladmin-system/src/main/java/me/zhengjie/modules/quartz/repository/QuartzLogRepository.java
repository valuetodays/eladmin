package me.zhengjie.modules.quartz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.quartz.domain.QuartzLog;

/**
 * @author Zheng Jie
 * @since 2019-01-07
 */
@ApplicationScoped
public class QuartzLogRepository extends MyPanacheRepository<QuartzLog> {

}
