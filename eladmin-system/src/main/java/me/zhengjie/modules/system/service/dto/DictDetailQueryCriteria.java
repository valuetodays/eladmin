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

import java.util.ArrayList;
import java.util.List;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DictDetailQueryCriteria extends PageIO implements QuerySearchable {

    @Schema(description = "字典标签")
    @Query(type = Query.Type.INNER_LIKE)
    private String label;

    @Schema(description = "字典名称")
    @Query(propName = "name",joinName = "dict")
    private String dictName;

    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();
        if (StringUtils.isNotBlank(label)) {
            querySearches.add(QuerySearch.of("label", label, Operator.LIKE));
        }
        if (StringUtils.isNotBlank(dictName)) {
            querySearches.add(QuerySearch.of("dictId#id#Dict#name", dictName, Operator.JOIN));
        }
        return querySearches;
    }
}
