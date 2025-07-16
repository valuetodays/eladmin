
package me.zhengjie.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Zheng Jie
* @date 2019-09-05
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