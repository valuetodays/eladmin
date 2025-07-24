package me.zhengjie.modules.mybiz.service.dto;

import cn.valuetodays.quarkus.commons.base.PageIO;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.QuerySearchable;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

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
    private Long name;

    /**
     * 精确
     */
    @Query
    @Schema(description = "timezone状态：1启用、0禁用")
    private Integer timeZoneEnabled;

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
    private Integer httpsEnabled;

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
    private Integer enabled;

    @Override
    public List<QuerySearch> toQuerySearches() {
        return null;
    }
}
