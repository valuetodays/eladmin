package me.zhengjie.modules.maint.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Data
public class ServerDeployQueryCriteria{

    @Schema(description = "模糊查询")
	@Query(blurry = "name,ip,account")
    private String blurry;

    @Schema(description = "创建时间")
	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> createTime;
}
