package me.zhengjie.service.dto;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author Zheng Jie
 * @since 2019-5-22
*/
@Data
public class SysLogErrorDto implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "方法")
    private String method;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "请求ip")
    private String requestIp;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "创建时间")
    private Timestamp createTime;
}