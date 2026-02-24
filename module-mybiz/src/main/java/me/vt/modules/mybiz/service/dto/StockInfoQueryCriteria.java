package me.vt.modules.mybiz.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ll.vt.quarkus.commons.base.Operator;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class StockInfoQueryCriteria extends PageIO implements QuerySearchable {

    /** 模糊 */
    @Schema(description = "编号")
    private String code;

    /** 模糊 */
    @Schema(description = "名称")
    private String shortName;

    /** 精确 */
    @Schema(description = "区域（上海，深圳，北京，香港）")
    private String region;

    @Schema(description = "是否流行")
    private Boolean popularFlag;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(code)) {
            querySearches.add(QuerySearch.of("code", code, Operator.EQ));
        }
        if (StringUtils.isNotBlank(shortName)) {
            querySearches.add(QuerySearch.of("shortName", shortName, Operator.LIKE));
        }
        if (StringUtils.isNotBlank(region)) {
            querySearches.add(QuerySearch.of("region", region, Operator.EQ));
        }
        if (Objects.nonNull(popularFlag)) {
            querySearches.add(QuerySearch.of("popularFlag", String.valueOf(popularFlag), Operator.EQ));
        }

        return querySearches;
    }
}
