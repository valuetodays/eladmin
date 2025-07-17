package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

/**
 * 日志查询类
 * @author Zheng Jie
 * @since 2019-6-4 09:23:07
 */
@Data
public class SysLogQueryCriteria {

    @Schema(description = "模糊查询")
    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    @Schema(description = "用户名")
    private String username;

    @Query
    @Schema(description = "日志类型")
    private String logType;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
