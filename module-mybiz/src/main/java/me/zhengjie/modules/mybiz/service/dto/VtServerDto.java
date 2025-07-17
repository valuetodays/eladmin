package me.zhengjie.modules.mybiz.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author vt
 * @website https://eladmin.vip
 * @description /
 * @since 2025-07-11
 **/
@Data
public class VtServerDto implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "绑定的端口，外网->内网")
    private String portBindings;

    @Schema(description = "timezone状态：1启用、0禁用")
    private Integer timeZoneEnabled;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "https状态：1启用、0禁用")
    private Integer httpsEnabled;

    @Schema(description = "镜像地址")
    private String imageName;

    @Schema(description = "状态：1启用、0禁用")
    private Integer enabled;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "创建日期")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}