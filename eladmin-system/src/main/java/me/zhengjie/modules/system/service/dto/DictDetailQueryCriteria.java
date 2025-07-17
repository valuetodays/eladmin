package me.zhengjie.modules.system.service.dto;

import cn.valuetodays.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DictDetailQueryCriteria extends PageIO {

    @Schema(description = "字典标签")
    @Query(type = Query.Type.INNER_LIKE)
    private String label;

    @Schema(description = "字典名称")
    @Query(propName = "name",joinName = "dict")
    private String dictName;
}