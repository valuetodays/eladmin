package me.zhengjie.modules.quartz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Zheng Jie
 * @since 2019-01-07
 */
@Getter
@Setter
@Entity
@Table(name = "sys_quartz_job")
public class QuartzJob extends BaseEntity implements Serializable {

    public static final String JOB_KEY = "JOB_KEY";

    @Id
    @Column(name = "job_id")
    @NotNull(groups = {Update.class})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    @Schema(description = "用于子任务唯一标识", hidden = true)
    private String uuid;

    @Schema(description = "定时器名称")
    private String jobName;

    @NotBlank
    @Schema(description = "Bean名称")
    private String beanName;

    @NotBlank
    @Schema(description = "方法名称")
    private String methodName;

    @Schema(description = "参数")
    private String params;

    @NotBlank
    @Schema(description = "cron表达式")
    private String cronExpression;

    @Schema(description = "状态，暂时或启动")
    @Column(name = "is_pause")
    private Boolean isPause;

    @Schema(description = "负责人")
    private String personInCharge;

    @Schema(description = "报警邮箱")
    private String email;

    @Schema(description = "子任务")
    private String subTask;

    @Schema(description = "失败后暂停")
    private Boolean pauseAfterFailure;

    @NotBlank
    @Schema(description = "备注")
    private String description;
}