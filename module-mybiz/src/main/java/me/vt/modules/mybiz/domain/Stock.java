package me.vt.modules.mybiz.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @author vt
* @since 2025-08-11 19:56
**/
@Entity
@Data
@Table(name="fortune_stock")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "主键")
    private Long id;

    @Column(name = "code",nullable = false)
    @NotBlank
    @Schema(description = "编号")
    private String code;

    @Column(name = "region",nullable = false)
    @NotBlank
    @Schema(description = "区域")
    private String region;

    @Column(name = "name",nullable = false)
    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Column(name = "remark")
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_user_id",nullable = false)
    @NotNull
    @Schema(description = "创建者")
    private Long createUserId;

    @Column(name = "update_user_id",nullable = false)
    @NotNull
    @Schema(description = "更新者")
    private Long updateUserId;

    @Column(name = "create_time",nullable = false)
    @NotNull
    @Schema(description = "创建日期")
    private LocalDateTime createTime;

    @Column(name = "update_time",nullable = false)
    @NotNull
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public void copy(Stock source) {
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
