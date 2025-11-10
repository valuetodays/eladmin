package me.vt.modules.quartz.service.dto;

import ll.vt.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Zheng Jie
 * @since 2019-6-4 10:33:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobQueryCriteria extends PageIO {

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
