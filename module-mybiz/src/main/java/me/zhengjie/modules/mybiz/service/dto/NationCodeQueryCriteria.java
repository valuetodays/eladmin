package me.zhengjie.modules.mybiz.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@Data
public class NationCodeQueryCriteria{

    /** 精确 */
    @Query
    @ApiModelProperty(value = "国家编码2位")
    private String code;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "国家编码3位")
    private String alpha3Code;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "国家数字编码")
    private String numeric;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "国家名称（英文）")
    private String englishName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "国家名称（中文）")
    private String chineseName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "货币符号")
    private String symbol;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "货币")
    private String currency;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "手机区号")
    private String phoneAreaCode;

    /** 精确 */
    @Query
    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Integer enabled;
}