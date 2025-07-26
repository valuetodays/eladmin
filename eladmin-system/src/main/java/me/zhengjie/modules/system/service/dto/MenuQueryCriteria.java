package me.zhengjie.modules.system.service.dto;

import cn.valuetodays.quarkus.commons.base.Operator;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import me.zhengjie.QuerySearchable;
import me.zhengjie.annotation.Query;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Zheng Jie
 * 公共查询类
 */
@Data
public class MenuQueryCriteria implements QuerySearchable {

    @Schema(description = "模糊查询")
    @Query(blurry = "title,component,permission")
    private String blurry;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    @Query
    @Schema(description = "PID")
    private Long pid;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(blurry)) {
            querySearches.add(QuerySearch.of("title,component,permission", blurry, Operator.MULTI_LIKE));
        }
        if (Objects.isNull(pid)) {
            querySearches.add(QuerySearch.of("pid", null, Operator.IS_NULL));
        } else {
            querySearches.add(QuerySearch.of("pid", String.valueOf(pid), Operator.EQ));
        }
        // fixme createTime between
//        if (StringUtils.isNotBlank(dictName)) {
//            querySearches.add(QuerySearch.of("dictId#id#Dict#name", dictName, Operator.JOIN));
//        }
        return querySearches;
    }
}
