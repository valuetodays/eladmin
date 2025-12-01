package ${package}.service.dto;

import lombok.Data;
<#if hasTimestamp>
import java.time.LocalDateTime;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if !auto && pkColumnType = 'Long'>
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.serializer.ToStringSerializer;
</#if>
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author ${author}
* @since ${.now?string("yyyy-MM-dd HH:mm")}
**/
@Data
public class ${className}Dto implements Serializable {
<#if columns??>
    <#list columns as column>

    <#if column.remark != ''>
    @Schema(description = "${column.remark}")
    <#else>
    @Schema(description = "${column.changeColumnName}")
    </#if>
    <#if column.columnKey = 'PRI'>
    <#if !auto && pkColumnType = 'Long'>
    </#if>
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
}
