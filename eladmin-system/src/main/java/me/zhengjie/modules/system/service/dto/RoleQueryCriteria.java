package me.zhengjie.modules.system.service.dto;

import java.sql.Timestamp;
import java.util.List;

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
public class RoleQueryCriteria extends PageIO {

    @Schema(description = "模糊查询")
    @Query(blurry = "name,description")
    private String blurry;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
