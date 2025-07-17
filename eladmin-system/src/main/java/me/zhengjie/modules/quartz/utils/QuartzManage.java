//package me.zhengjie.modules.quartz.utils;
//
//import java.util.Date;
//
//import jakarta.annotation.Resource;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import lombok.extern.slf4j.Slf4j;
//import me.zhengjie.exception.BadRequestException;
//import me.zhengjie.modules.quartz.domain.QuartzJob;
//import org.quartz.*;
//import org.quartz.impl.triggers.CronTriggerImpl;
//
//import static org.quartz.TriggerBuilder.newTrigger;
// fixme
///**
// * @author Zheng Jie
// * @since 2019-01-07
// */
//@Slf4j
//@ApplicationScoped
//public class QuartzManage {
//
//    private static final String JOB_NAME = "TASK_";
//
//    @Inject
//    private Scheduler scheduler;
//
//    public void addJob(QuartzJob quartzJob){
//        try {
//            // 构建job信息
//            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
//                    withIdentity(JOB_NAME + quartzJob.getId()).build();
//
//            //通过触发器名和cron 表达式创建 Trigger
//            Trigger cronTrigger = newTrigger()
//                    .withIdentity(JOB_NAME + quartzJob.getId())
//                    .startNow()
//                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
//                    .build();
//
//            cronTrigger.getJobDataMap().put(QuartzJob.JOB_KEY, quartzJob);
//
//            //重置启动时间
//            ((CronTriggerImpl)cronTrigger).setStartTime(new Date());
//
//            //执行定时任务，如果是持久化的，这里会报错，捕获输出
//            try {
//                scheduler.scheduleJob(jobDetail,cronTrigger);
//            } catch (ObjectAlreadyExistsException e) {
//                log.warn("定时任务已存在，跳过加载");
//            }
//
//            // 暂停任务
//            if (quartzJob.getIsPause()) {
//                pauseJob(quartzJob);
//            }
//        } catch (Exception e){
//            log.error("创建定时任务失败", e);
//            throw new BadRequestException("创建定时任务失败");
//        }
//    }
//
//    /**
//     * 更新job cron表达式
//     * @param quartzJob /
//     */
//    public void updateJobCron(QuartzJob quartzJob){
//        try {
//            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
//            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//            // 如果不存在则创建一个定时任务
//            if(trigger == null){
//                addJob(quartzJob);
//                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//            }
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
//            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
//            //重置启动时间
//            ((CronTriggerImpl)trigger).setStartTime(new Date());
//            trigger.getJobDataMap().put(QuartzJob.JOB_KEY,quartzJob);
//
//            scheduler.rescheduleJob(triggerKey, trigger);
//            // 暂停任务
//            if (quartzJob.getIsPause()) {
//                pauseJob(quartzJob);
//            }
//        } catch (Exception e){
//            log.error("更新定时任务失败", e);
//            throw new BadRequestException("更新定时任务失败");
//        }
//
//    }
//
//    /**
//     * 删除一个job
//     * @param quartzJob /
//     */
//    public void deleteJob(QuartzJob quartzJob){
//        try {
//            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
//            scheduler.pauseJob(jobKey);
//            scheduler.deleteJob(jobKey);
//        } catch (Exception e){
//            log.error("删除定时任务失败", e);
//            throw new BadRequestException("删除定时任务失败");
//        }
//    }
//
//    /**
//     * 恢复一个job
//     * @param quartzJob /
//     */
//    public void resumeJob(QuartzJob quartzJob){
//        try {
//            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
//            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//            // 如果不存在则创建一个定时任务
//            if(trigger == null) {
//                addJob(quartzJob);
//            }
//            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
//            scheduler.resumeJob(jobKey);
//        } catch (Exception e){
//            log.error("恢复定时任务失败", e);
//            throw new BadRequestException("恢复定时任务失败");
//        }
//    }
//
//    /**
//     * 立即执行job
//     * @param quartzJob /
//     */
//    public void runJobNow(QuartzJob quartzJob){
//        try {
//            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
//            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//            // 如果不存在则创建一个定时任务
//            if(trigger == null) {
//                addJob(quartzJob);
//            }
//            JobDataMap dataMap = new JobDataMap();
//            dataMap.put(QuartzJob.JOB_KEY, quartzJob);
//            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
//            scheduler.triggerJob(jobKey,dataMap);
//        } catch (Exception e){
//            log.error("定时任务执行失败", e);
//            throw new BadRequestException("定时任务执行失败");
//        }
//    }
//
//    /**
//     * 暂停一个job
//     * @param quartzJob /
//     */
//    public void pauseJob(QuartzJob quartzJob){
//        try {
//            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
//            scheduler.pauseJob(jobKey);
//        } catch (Exception e){
//            log.error("定时任务暂停失败", e);
//            throw new BadRequestException("定时任务暂停失败");
//        }
//    }
//}
