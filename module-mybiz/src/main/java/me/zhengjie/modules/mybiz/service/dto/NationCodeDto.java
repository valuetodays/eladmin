package me.zhengjie.modules.mybiz.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description /
* @author vt
* @since 2025-07-14 22:15
**/
@Data
public class NationCodeDto implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "国家编码2位")
    private String code;

    @ApiModelProperty(value = "国家编码3位")
    private String alpha3Code;

    @ApiModelProperty(value = "国家数字编码")
    private String numeric;

    @ApiModelProperty(value = "国家名称（英文）")
    private String englishName;

    @ApiModelProperty(value = "国家名称（中文）")
    private String chineseName;

    @ApiModelProperty(value = "货币符号")
    private String symbol;

    @ApiModelProperty(value = "货币")
    private String currency;

    @ApiModelProperty(value = "手机区号")
    private String phoneAreaCode;

    @ApiModelProperty(value = "国旗")
    private String flag;

    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Integer enabled;

    @ApiModelProperty(value = "createTime")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "updateTime")
    private LocalDateTime updateTime;
}