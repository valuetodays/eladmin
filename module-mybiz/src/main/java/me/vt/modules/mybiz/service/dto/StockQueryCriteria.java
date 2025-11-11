
package me.vt.modules.mybiz.service.dto;

import java.util.ArrayList;
import java.util.List;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;
import me.vt.common.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author vt
 * @since 2025-08-11 19:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class StockQueryCriteria extends PageIO implements QuerySearchable {

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

    @Override
    public List<QuerySearch> toQuerySearches() {
        return new ArrayList<>();
    }
}
