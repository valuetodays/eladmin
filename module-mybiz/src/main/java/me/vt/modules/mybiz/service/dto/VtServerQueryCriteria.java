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
import me.vt.common.annotation.Query;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author vt

 * @since 2025-07-11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class VtServerQueryCriteria extends PageIO implements QuerySearchable {

    /**
     * 精确
     */
    @Query
    @Schema(description = "名称")
    private String name;

    /**
     * 精确
     */
    @Query
    @Schema(description = "timezone状态：1启用、0禁用")
    private Boolean timeZoneEnabled;

    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "域名")
    private String domain;

    /**
     * 精确
     */
    @Query
    @Schema(description = "https状态：1启用、0禁用")
    private Boolean httpsEnabled;

    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "镜像地址")
    private String imageName;

    /**
     * 精确
     */
    @Query
    @Schema(description = "状态：1启用、0禁用")
    private Boolean enabled;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            querySearches.add(QuerySearch.of("name", name, Operator.LIKE));
        }
        if (Objects.nonNull(timeZoneEnabled)) {
            querySearches.add(QuerySearch.of("timeZoneEnabled", timeZoneEnabled.toString(), Operator.EQ));
        }
        if (StringUtils.isNotBlank(domain)) {
            querySearches.add(QuerySearch.of("domain", domain, Operator.LIKE));
        }
        if (Objects.nonNull(httpsEnabled)) {
            querySearches.add(QuerySearch.of("httpsEnabled", httpsEnabled.toString(), Operator.EQ));
        }
        if (StringUtils.isNotBlank(imageName)) {
            querySearches.add(QuerySearch.of("imageName", imageName, Operator.LIKE));
        }
        if (Objects.nonNull(enabled)) {
            querySearches.add(QuerySearch.of("enabled", enabled.toString(), Operator.EQ));
        }
        return querySearches;
    }
}
