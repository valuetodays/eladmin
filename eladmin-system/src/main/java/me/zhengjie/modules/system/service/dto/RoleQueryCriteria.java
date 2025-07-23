package me.zhengjie.modules.system.service.dto;

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
 * @author Zheng Jie
 * 公共查询类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryCriteria extends PageIO implements QuerySearchable {

    @Schema(description = "模糊查询")
    @Query(blurry = "name,description")
    private String blurry;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(blurry)) {
            querySearches.add(QuerySearch.of("name,description", blurry, Operator.MULTI_LIKE));
        }
        // fixme createTime between
//        if (StringUtils.isNotBlank(dictName)) {
//            querySearches.add(QuerySearch.of("dictId#id#Dict#name", dictName, Operator.JOIN));
//        }
        return querySearches;
    }
}
