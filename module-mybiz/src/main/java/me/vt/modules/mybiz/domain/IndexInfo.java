package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@Entity
@Data
@Table(name = "f_index_info")
public class IndexInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "主键")
    private Long id;

    @Column(name = "code", nullable = false)
    @NotBlank
    @Schema(description = "编号")
    private String code;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Column(name = "region", nullable = false)
    @NotBlank
    @Schema(description = "区域（上海，深圳，北京，香港）")
    private String region;

    @Column(name = "description")
    @Schema(description = "描述")
    private String description;

    @Column(name = "release_date")
    @Schema(description = "发布日期")
    private LocalDate releaseDate;

    @Column(name = "data_base_date")
    @Schema(description = "基日")
    private LocalDate dataBaseDate;

    @Column(name = "data_base_val")
    @Schema(description = "基点")
    private BigDecimal dataBaseVal;

    @Column(name = "popular_flag", nullable = false)
    @NotNull
    @Schema(description = "是否常见")
    private Boolean popularFlag;

    @Column(name = "suggest_etfs")
    @Schema(description = "建议的etf列表")
    private String suggestEtfs;

    @Column(name = "adj_freq")
    @Schema(description = "调仓频率")
    private String adjFreq;

    @Column(name = "create_time", nullable = false)
    @NotNull
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    @NotNull
    @Schema(description = "修改日期")
    private LocalDateTime updateTime;

    @Column(name = "create_user_id", nullable = false)
    @NotNull
    @Schema(description = "创建者id")
    private Long createUserId;

    @Column(name = "update_user_id", nullable = false)
    @NotNull
    @Schema(description = "更新者id")
    private Long updateUserId;

    public void copy(IndexInfo source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
