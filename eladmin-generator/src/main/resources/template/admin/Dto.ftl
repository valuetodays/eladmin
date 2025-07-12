package ${package}.service.dto;

import lombok.Data;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if !auto && pkColumnType = 'Long'>
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.serializer.ToStringSerializer;
</#if>
import io.swagger.annotations.ApiModelProperty;

/**
* @website https://eladmin.vip
* @description /
* @author ${author}
* @date ${date}
**/
@Data
public class ${className}Dto implements Serializable {
<#if columns??>
    <#list columns as column>

    <#if column.remark != ''>
    @ApiModelProperty(value = "${column.remark}")
    <#else>
    @ApiModelProperty(value = "${column.changeColumnName}")
    </#if>
    <#if column.columnKey = 'PRI'>
    <#if !auto && pkColumnType = 'Long'>
    /** 防止精度丢失 */
    @JSONField(serializeUsing = ToStringSerializer.class)
    </#if>
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
}