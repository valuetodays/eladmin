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
public class SysLogSmallDto implements Serializable {

    @Schema(description = "描述")
    private String description;

    @Schema(description = "请求IP")
    private String requestIp;

    @Schema(description = "耗时")
    private Long time;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "创建时间")
    private Timestamp createTime;
}
