package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@Data
public class LocalStorageQueryCriteria{

    @Schema(description = "模糊查询")
    @Query(blurry = "name,suffix,type,createBy,size")
    private String blurry;

    @Schema(description = "创建时间")
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}