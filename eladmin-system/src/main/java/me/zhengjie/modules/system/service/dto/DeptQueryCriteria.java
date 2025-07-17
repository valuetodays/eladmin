package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.DataPermission;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@Data
@DataPermission(fieldName = "id")
public class DeptQueryCriteria{

    @Schema(description = "名称")
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Query
    @Schema(description = "上级部门")
    private Long pid;

    @Schema(description = "PID空查询", hidden = true)
    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}