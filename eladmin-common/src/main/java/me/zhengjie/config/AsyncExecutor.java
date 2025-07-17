//
//package me.zhengjie.config;
//
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
///**
// * 创建自定义的线程池
// * @author Zheng Jie
// * @description
// * @since 2023-06-08
// **/
//@EnableAsync
//@Configuration
//public class AsyncExecutor implements AsyncConfigurer {
//
//    public static int corePoolSize;
//
//    public static int maxPoolSize;
//
//    public static int keepAliveSeconds;
//
//    public static int queueCapacity;
//
//    @Value("${task.pool.core-pool-size}")
//    public void setCorePoolSize(int corePoolSize) {
//        AsyncExecutor.corePoolSize = corePoolSize;
//    }
//
//    @Value("${task.pool.max-pool-size}")
//    public void setMaxPoolSize(int maxPoolSize) {
//        AsyncExecutor.maxPoolSize = maxPoolSize;
//    }
//
//    @Value("${task.pool.keep-alive-seconds}")
//    public void setKeepAliveSeconds(int keepAliveSeconds) {
//        AsyncExecutor.keepAliveSeconds = keepAliveSeconds;
//    }
//
//    @Value("${task.pool.queue-capacity}")
//    public void setQueueCapacity(int queueCapacity) {
//        AsyncExecutor.queueCapacity = queueCapacity;
//    }
//
//    /**
//     * 自定义线程池，用法 @Async
//     * @return Executor
//     */
//    @Override
//    public Executor getAsyncExecutor() {
//        // 自定义工厂
//        ThreadFactory factory = r -> new Thread(r, "el-async-" + new AtomicInteger(1).getAndIncrement());
//        // 自定义线程池
//        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueCapacity), factory,
//                new ThreadPoolExecutor.CallerRunsPolicy());
//    }
//
//    /**
//     * 自定义线程池，用法，注入到类中使用
//     * private ThreadPoolTaskExecutor taskExecutor;
//     * @return ThreadPoolTaskExecutor
//     */
//    @Bean("taskAsync")
//    public ThreadPoolTaskExecutor taskAsync() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(4);
//        executor.setQueueCapacity(20);
//        executor.setKeepAliveSeconds(60);
//        executor.setThreadNamePrefix("el-task-");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        return executor;
//    }
//}
