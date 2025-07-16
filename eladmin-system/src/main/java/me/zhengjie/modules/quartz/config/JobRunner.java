
package me.zhengjie.modules.quartz.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.quartz.domain.QuartzJob;
import me.zhengjie.modules.quartz.repository.QuartzJobRepository;
import me.zhengjie.modules.quartz.utils.QuartzManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
    @Inject
    QuartzJobRepository quartzJobRepository;
    @Inject
    QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        // todo disable temply
        List<QuartzJob> quartzJobs = quartzJobRepository.findByIsPauseFalse();
        quartzJobs.forEach(quartzManage::addJob);
        log.info("Timing task injection complete");
    }
}
