package me.zhengjie.modules.system.service.dto;

import cn.valuetodays.quarkus.commons.base.Operator;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import me.zhengjie.QuerySearchable;
import me.zhengjie.annotation.DataPermission;
import me.zhengjie.annotation.Query;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@Data
@DataPermission(fieldName = "id")
public class DeptQueryCriteria implements QuerySearchable {

    @Schema(description = "名称")
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Query
    @Schema(description = "上级部门")
    private Long pid;

    @Schema(description = "PID空查询", hidden = true)
    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<LocalDateTime> createTime;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            querySearches.add(QuerySearch.of("name", name, Operator.LIKE));
        }
        if (Objects.nonNull(enabled)) {
            querySearches.add(QuerySearch.of("enabled", enabled.toString(), Operator.EQ));
        }
        if (Objects.nonNull(pid)) {
            querySearches.add(QuerySearch.of("pid", pid.toString(), Operator.EQ));
        }
        // fixme: add IS_NULL  / IS_NOT_NULL
//        querySearches.add(QuerySearch.of("pid", pid, Operator.IS_NULL));
        // fixme between
//        querySearches.add(QuerySearch.of("createTime", createTime, Operator.DATE_BETWEEN));
        return querySearches;
    }
}
