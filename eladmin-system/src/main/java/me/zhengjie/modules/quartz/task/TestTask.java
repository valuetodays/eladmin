package me.zhengjie.modules.quartz.task;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试用
 * @author Zheng Jie
 * @since 2019-01-08
 */
@Slf4j
@ApplicationScoped
public class TestTask {

    public void run(){
        log.info("run 执行成功");
    }

    public void run1(String str){
        log.info("run1 执行成功，参数为： {}", str);
    }

    public void run2(){
        log.info("run2 执行成功");
    }
}
