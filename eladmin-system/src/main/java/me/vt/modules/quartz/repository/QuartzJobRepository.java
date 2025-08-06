package me.vt.modules.quartz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.quartz.domain.QuartzJob;

import java.util.List;

/**
 * @author Zheng Jie
 * @since 2019-01-07
 */
@ApplicationScoped
public class QuartzJobRepository extends MyPanacheRepository<QuartzJob> {

    /**
     * 查询启用的任务
     * @return List
     */
    public List<QuartzJob> findByIsPauseFalse() {
        return find("isPause = false").list();
    }
}
