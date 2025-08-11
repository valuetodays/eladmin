
package me.vt.modules.mybiz.service.dto;

import lombok.Data;

import me.vt.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author vt
 * @since 2025-08-11 19:56
 **/
@Data
public class StockQueryCriteria {

    /**
     * 精确
     */
    @Query
    @Schema(description = "编号")
    private String code;

    /**
     * 精确
     */
    @Query
    @Schema(description = "区域")
    private String region;

    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "名称")
    private String name;

    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "备注")
    private String remark;
}
