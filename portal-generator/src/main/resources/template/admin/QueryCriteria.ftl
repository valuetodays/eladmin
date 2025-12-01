package ${package}.service.dto;

import lombok.Data;
<#if queryHasTimestamp>
import java.sql.Timestamp;
</#if>
<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if betweens??>
import java.util.List;
</#if>
<#if queryColumns??>
import me.vt.annotation.Query;
</#if>
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ll.vt.quarkus.commons.base.PageIO;
import ll.vt.quarkus.commons.base.QuerySearch;

/**
* @author ${author}
* @since ${.now?string("yyyy-MM-dd HH:mm")}
**/
@Data
public class ${className}QueryCriteria extends PageIO implements QuerySearchable {
<#if queryColumns??>
    <#list queryColumns as column>

<#if column.queryType = '='>
    /** 精确 */
    @Query
    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = 'Like'>
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '!='>
    /** 不等于 */
    @Query(type = Query.Type.NOT_EQUAL)
    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = 'NotNull'>
    /** 不为空 */
    @Query(type = Query.Type.NOT_NULL)
    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '>='>
    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '<='>
    /** 小于等于 */
    @Query(type = Query.Type.LESS_THAN)
    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
</#if>
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<${column.columnType}> ${column.changeColumnName};
    </#list>
</#if>


    @Override
    public List<QuerySearch> toQuerySearches() {
        List<QuerySearch> querySearches = new ArrayList<>();

        return querySearches;
    }
}
