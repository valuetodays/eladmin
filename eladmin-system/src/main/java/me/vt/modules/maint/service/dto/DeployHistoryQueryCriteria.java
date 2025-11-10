package me.vt.modules.maint.service.dto;

import ll.vt.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DeployHistoryQueryCriteria extends PageIO {

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
