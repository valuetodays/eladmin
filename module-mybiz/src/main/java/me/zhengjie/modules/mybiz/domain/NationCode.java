package me.zhengjie.modules.mybiz.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`code`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "国家编码2位")
    private String code;

    @Column(name = "`alpha_3_code`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "国家编码3位")
    private String alpha3Code;

    @Column(name = "`numeric`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "国家数字编码")
    private String numeric;

    @Column(name = "`english_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "国家名称（英文）")
    private String englishName;

    @Column(name = "`chinese_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "国家名称（中文）")
    private String chineseName;

    @Column(name = "`symbol`")
    @ApiModelProperty(value = "货币符号")
    private String symbol;

    @Column(name = "`currency`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "货币")
    private String currency;

    @Column(name = "`phone_area_code`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "手机区号")
    private String phoneAreaCode;

    @Column(name = "`flag`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "国旗")
    private String flag;

    @Column(name = "`enabled`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Integer enabled;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "createTime")
    private LocalDateTime createTime;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "updateTime")
    private LocalDateTime updateTime;

    public void copy(NationCode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
