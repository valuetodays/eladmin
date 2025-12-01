package me.vt.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * 表的数据信息
 * @author Zheng Jie
 * @since 2019-01-02
 */
@Data
@AllArgsConstructor
public class TableInfo {

    @Schema(description = "表名称")
    private String tableName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期：yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @Schema(description = "数据库引擎")
    private String engine;

    @Schema(description = "编码集")
    private String coding;

    @Schema(description = "备注")
    private String remark;
}
