package me.vt.modules.mybiz.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.base.BaseDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author vt

 * @description /
 * @since 2025-07-11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class VtServerDto extends BaseDTO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "绑定的端口，外网->内网")
    private String portBindings;

    @Schema(description = "timezone状态：1启用、0禁用")
    private Boolean timeZoneEnabled;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "https状态：1启用、0禁用")
    private Boolean httpsEnabled;

    @Schema(description = "镜像地址")
    private String imageName;

    @Schema(description = "状态：1启用、0禁用")
    private Boolean enabled;

}
