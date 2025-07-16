
package me.zhengjie.modules.quartz.repository;

import java.util.List;

import me.zhengjie.modules.quartz.domain.QuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
public interface QuartzJobRepository extends JpaRepository<QuartzJob,Long>, JpaSpecificationExecutor<QuartzJob> {

    /**
     * 查询启用的任务
     * @return List
     */
    List<QuartzJob> findByIsPauseFalse();
}
