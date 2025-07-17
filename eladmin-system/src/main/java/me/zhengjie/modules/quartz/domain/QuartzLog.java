package me.zhengjie.modules.quartz.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Zheng Jie
 * @since 2019-01-07
 */
@Entity
@Data
@Table(name = "sys_quartz_log")
public class QuartzLog implements Serializable {

    @Id
    @Column(name = "log_id")
    @Schema(description = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "任务名称", hidden = true)
    private String jobName;

    @Schema(description = "bean名称", hidden = true)
    private String beanName;

    @Schema(description = "方法名称", hidden = true)
    private String methodName;

    @Schema(description = "参数", hidden = true)
    private String params;

    @Schema(description = "cron表达式", hidden = true)
    private String cronExpression;

    @Schema(description = "状态", hidden = true)
    private Boolean isSuccess;

    @Schema(description = "异常详情", hidden = true)
    private String exceptionDetail;

    @Schema(description = "执行耗时", hidden = true)
    private Long time;

    @CreationTimestamp
    @Schema(description = "创建时间", hidden = true)
    private Timestamp createTime;
}
