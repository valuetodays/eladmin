package me.vt.modules.mybiz.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-11-18 20:01
**/
@Data
public class Cci14_100DataDto implements Serializable {
    @Schema(description = "code")
    private String code;

    @Schema(description = "name")
    private String name;

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "cci14")
    private BigDecimal cci14;
}
