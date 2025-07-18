package me.zhengjie.modules.system.service.dto;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.valuetodays.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryCriteria extends PageIO {

    @Query
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "部门ID集合")
    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds = new HashSet<>();

    @Schema(description = "模糊查询")
    @Query(blurry = "email,username,nickName")
    private String blurry;

    @Query
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
