package me.zhengjie.modules.system.service.dto;

import cn.valuetodays.quarkus.commons.base.Operator;
import cn.valuetodays.quarkus.commons.base.PageIO;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.QuerySearchable;
import me.zhengjie.annotation.Query;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryCriteria extends PageIO implements QuerySearchable {

    @Query
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "部门ID集合")
    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds = new HashSet<>();

    @Schema(description = "模糊查询")
    @Query(blurry = "email,username,nickName")
    private String blurry;

    @Query
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deptIds)) {
            querySearches.add(QuerySearch.of("deptId", StringUtils.join(deptIds, ","), Operator.IN));
        }
        if (StringUtils.isNotBlank(blurry)) {
            querySearches.add(QuerySearch.of("email,username,nickName", blurry, Operator.MULTI_LIKE));
        }
        if (Objects.nonNull(enabled)) {
            querySearches.add(QuerySearch.of("enabled", enabled.toString(), Operator.EQ));
        }
        // fixme: add IS_NULL  / IS_NOT_NULL
//        querySearches.add(QuerySearch.of("pid", pid, Operator.IS_NULL));
        // fixme between
//        querySearches.add(QuerySearch.of("createTime", createTime, Operator.DATE_BETWEEN));
        return querySearches;
    }

}
