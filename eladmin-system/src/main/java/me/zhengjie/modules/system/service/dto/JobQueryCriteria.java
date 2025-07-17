package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
* @author Zheng Jie
 * @since 2019-6-4 14:49:34
*/
@Data
@NoArgsConstructor
public class JobQueryCriteria {

    @Schema(description = "岗位名称")
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    @Schema(description = "岗位状态")
    private Boolean enabled;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}