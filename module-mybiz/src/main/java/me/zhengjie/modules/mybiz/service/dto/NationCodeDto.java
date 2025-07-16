package me.zhengjie.modules.mybiz.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
* @description /
* @author vt
* @since 2025-07-14 22:15
**/
@Data
public class NationCodeDto implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "国家编码2位")
    private String code;

    @Schema(description = "国家编码3位")
    private String alpha3Code;

    @Schema(description = "国家数字编码")
    private String numeric;

    @Schema(description = "国家名称（英文）")
    private String englishName;

    @Schema(description = "国家名称（中文）")
    private String chineseName;

    @Schema(description = "货币符号")
    private String symbol;

    @Schema(description = "货币")
    private String currency;

    @Schema(description = "手机区号")
    private String phoneAreaCode;

    @Schema(description = "国旗")
    private String flag;

    @Schema(description = "状态：1启用、0禁用")
    private Integer enabled;

    @Schema(description = "createTime")
    private LocalDateTime createTime;

    @Schema(description = "updateTime")
    private LocalDateTime updateTime;
}