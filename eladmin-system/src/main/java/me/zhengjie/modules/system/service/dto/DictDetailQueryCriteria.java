
package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Data
public class DictDetailQueryCriteria {

    @Schema(description = "字典标签")
    @Query(type = Query.Type.INNER_LIKE)
    private String label;

    @Schema(description = "字典名称")
    @Query(propName = "name",joinName = "dict")
    private String dictName;
}