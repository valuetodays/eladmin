
package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author Zheng Jie
 * 公共查询类
 */
@Data
public class DictQueryCriteria {

    @Schema(description = "模糊查询")
    @Query(blurry = "name,description")
    private String blurry;
}
