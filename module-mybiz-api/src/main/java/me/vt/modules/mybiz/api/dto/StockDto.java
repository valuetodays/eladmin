package me.vt.modules.mybiz.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.vt.common.base.BaseDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author vt
 * @since 2025-08-11 19:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class StockDto extends BaseDTO {

    @Schema(description = "编号")
    private String code;

    @Schema(description = "区域")
    private String region;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;
}
