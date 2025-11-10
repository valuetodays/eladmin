package me.vt.modules.mybiz.service.dto;

import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;
import me.vt.common.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class NationCodeQueryCriteria extends PageIO implements QuerySearchable {

    /** 精确 */
    @Query
    @Schema(description = "国家编码2位")
    private String code;

    /** 精确 */
    @Query
    @Schema(description = "国家编码3位")
    private String alpha3Code;

    /** 精确 */
    @Query
    @Schema(description = "国家数字编码")
    private String numeric;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "国家名称（英文）")
    private String englishName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "国家名称（中文）")
    private String chineseName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "货币符号")
    private String symbol;

    /** 精确 */
    @Query
    @Schema(description = "货币")
    private String currency;

    /** 精确 */
    @Query
    @Schema(description = "手机区号")
    private String phoneAreaCode;

    /** 精确 */
    @Query
    @Schema(description = "状态：1启用、0禁用")
    private Boolean enabled;


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        return querySearches;
    }
}
