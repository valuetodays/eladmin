package me.zhengjie.modules.system.service.dto;

import cn.valuetodays.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author Zheng Jie
 * 公共查询类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictQueryCriteria extends PageIO {

    @Schema(description = "模糊查询")
    @Query(blurry = "name,description")
    private String blurry;
}
