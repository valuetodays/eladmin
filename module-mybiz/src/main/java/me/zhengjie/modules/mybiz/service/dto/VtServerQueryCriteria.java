
package me.zhengjie.modules.mybiz.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author vt
 * @website https://eladmin.vip
 * @date 2025-07-11
 **/
@Data
public class VtServerQueryCriteria {

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
}