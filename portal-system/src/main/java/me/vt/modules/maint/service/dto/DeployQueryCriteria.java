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
public class DeployQueryCriteria extends PageIO {

    @Schema(description = "应用名称")
    @Query(type = Query.Type.INNER_LIKE, propName = "name", joinName = "app")
    private String appName;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

}
