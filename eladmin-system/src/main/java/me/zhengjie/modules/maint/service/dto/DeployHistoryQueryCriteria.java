
package me.zhengjie.modules.maint.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Data
public class DeployHistoryQueryCriteria{

    @Schema(description = "模糊查询")
	@Query(blurry = "appName,ip,deployUser")
	private String blurry;

	@Query
    @Schema(description = "部署编号")
	private Long deployId;

    @Schema(description = "部署时间")
	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> deployDate;
}
