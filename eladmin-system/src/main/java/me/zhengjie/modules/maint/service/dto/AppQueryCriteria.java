package me.zhengjie.modules.maint.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Data
public class AppQueryCriteria{

    @Schema(description = "模糊")
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Schema(description = "创建时间")
	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> createTime;
}
