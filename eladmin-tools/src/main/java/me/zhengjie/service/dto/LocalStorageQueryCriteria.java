package me.zhengjie.service.dto;

import java.sql.Timestamp;
import java.util.List;

import cn.valuetodays.quarkus.commons.base.PageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class LocalStorageQueryCriteria extends PageIO {

    @Schema(description = "模糊查询")
    @Query(blurry = "name,suffix,type,createBy,size")
    private String blurry;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}