package me.vt.modules.mybiz.service.dto;

import java.util.ArrayList;
import java.util.List;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class IndexInfoQueryCriteria extends PageIO implements QuerySearchable {

    /** 模糊 */
    @Schema(description = "编号")
    private String code;

    /** 模糊 */
    @Schema(description = "名称")
    private String name;

    /** 精确 */
    @Schema(description = "区域（上海，深圳，北京，香港）")
    private String region;


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();

        return querySearches;
    }
}
