
package me.zhengjie.modules.quartz.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author Zheng Jie
 * @date 2019-6-4 10:33:02
 */
@Data
public class JobQueryCriteria {

    @Schema(description = "任务名称")
    @Query(type = Query.Type.INNER_LIKE)
    private String jobName;

    @Query
    @Schema(description = "是否成功")
    private Boolean isSuccess;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
