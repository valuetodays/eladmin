package me.zhengjie.modules.maint.service.dto;

import java.sql.Timestamp;
import java.util.List;

import cn.valuetodays.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerDeployQueryCriteria extends PageIO {

    @Schema(description = "模糊查询")
	@Query(blurry = "name,ip,account")
    private String blurry;

    @Schema(description = "创建时间")
	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> createTime;
}
