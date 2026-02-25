package me.vt.modules.mybiz.service.dto;

import java.util.ArrayList;
import java.util.List;

import ll.vt.quarkus.commons.base.Operator;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.QuerySearchable;
import me.vt.common.annotation.Query;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class HttpsDomainQueryCriteria extends PageIO implements QuerySearchable {

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "标题")
    private String title;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "域名")
    private String domain;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "备注")
    private String remark;


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(title)) {
            querySearches.add(QuerySearch.of("title", title, Operator.LIKE));
        }
        if (StringUtils.isNotBlank(domain)) {
            querySearches.add(QuerySearch.of("domain", domain, Operator.LIKE));
        }
        if (StringUtils.isNotBlank(remark)) {
            querySearches.add(QuerySearch.of("remark", remark, Operator.LIKE));
        }
        return querySearches;
    }
}
