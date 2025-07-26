package me.zhengjie.service.dto;

import cn.valuetodays.quarkus.commons.base.Operator;
import cn.valuetodays.quarkus.commons.base.PageIO;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.QuerySearchable;
import me.zhengjie.annotation.Query;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志查询类
 * @author Zheng Jie
 * @since 2019-6-4 09:23:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogQueryCriteria extends PageIO implements QuerySearchable {

    @Schema(description = "模糊查询")
    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    @Schema(description = "用户名")
    private String username;

    @Query
    @Schema(description = "日志类型")
    private String logType;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(blurry)) {
            querySearches.add(QuerySearch.of("username,description,address,requestIp,method,params", blurry, Operator.MULTI_LIKE));
        }
        if (StringUtils.isNotBlank(username)) {
            querySearches.add(QuerySearch.of("username", username, Operator.LIKE));
        }
        if (StringUtils.isNotBlank(logType)) {
            querySearches.add(QuerySearch.of("logType", logType, Operator.EQ));
        }
        // fixme createTime between
//        if (StringUtils.isNotBlank(dictName)) {
//            querySearches.add(QuerySearch.of("dictId#id#Dict#name", dictName, Operator.JOIN));
//        }
        return querySearches;
    }
}
