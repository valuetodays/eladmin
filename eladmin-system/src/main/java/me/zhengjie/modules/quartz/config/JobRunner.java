//package me.zhengjie.modules.quartz.config;
//
//import java.util.List;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import lombok.RequiredArgsConstructor;
//import me.zhengjie.modules.quartz.domain.QuartzJob;
//import me.zhengjie.modules.quartz.repository.QuartzJobRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author Zheng Jie
// * @since 2019-01-07
// */
//@ApplicationScoped
//@RequiredArgsConstructor
//public class JobRunner implements ApplicationRunner {
//    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
//    @Inject
//    QuartzJobRepository quartzJobRepository;
//    @Inject
//    QuartzManage quartzManage;
//
//    /**
//     * 项目启动时重新激活启用的定时任务
//     *
//     * @param applicationArguments /
//     */
//    @Override
//    public void run(ApplicationArguments applicationArguments) {
//        // todo disable temply
//        List<QuartzJob> quartzJobs = quartzJobRepository.findByIsPauseFalse();
//        quartzJobs.forEach(quartzManage::addJob);
//        log.info("Timing task injection complete");
//    }
//}
