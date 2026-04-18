package me.vt.modules.maint.service.dto;

import com.vt.quarkus.commons.base.PageIO;
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
public class ServerDeployQueryCriteria extends PageIO {

    @Schema(description = "模糊查询")
    @Query(blurry = "name,ip,account")
    private String blurry;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
