package me.zhengjie.modules.mybiz.domain;

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
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @description /
* @author vt
* @since 2025-07-14 22:15
**/
@Entity
@Data
@Table(name="nation_code")
public class NationCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    @NotBlank
    @Schema(description = "国家编码2位")
    private String code;

    @Column(name = "alpha_3_code", nullable = false)
    @NotBlank
    @Schema(description = "国家编码3位")
    private String alpha3Code;

    @Column(name = "numeric", nullable = false)
    @NotBlank
    @Schema(description = "国家数字编码")
    private String numeric;

    @Column(name = "english_name", nullable = false)
    @NotBlank
    @Schema(description = "国家名称（英文）")
    private String englishName;

    @Column(name = "chinese_name", nullable = false)
    @NotBlank
    @Schema(description = "国家名称（中文）")
    private String chineseName;

    @Column(name = "symbol")
    @Schema(description = "货币符号")
    private String symbol;

    @Column(name = "currency", nullable = false)
    @NotBlank
    @Schema(description = "货币")
    private String currency;

    @Column(name = "phone_area_code", nullable = false)
    @NotBlank
    @Schema(description = "手机区号")
    private String phoneAreaCode;

    @Column(name = "flag", nullable = false)
    @NotBlank
    @Schema(description = "国旗")
    private String flag;

    @Column(name = "enabled", nullable = false)
    @NotNull
    @Schema(description = "状态：1启用、0禁用")
    private Integer enabled;

    @Column(name = "create_by", nullable = false)
    @NotBlank
    @Schema(description = "创建者")
    private String createBy;

    @Column(name = "update_by", nullable = false)
    @NotBlank
    @Schema(description = "更新者")
    private String updateBy;

    @Column(name = "create_time")
    @Schema(description = "createTime")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "updateTime")
    private LocalDateTime updateTime;

    public void copy(NationCode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
