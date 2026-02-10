package me.vt.modules.mybiz.api.dto;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class StockInfoDto implements Serializable {

    @Schema(description = "主键")
    private Long id;


    @Column(name = "code", nullable = false)
    @Schema(description = "编号")
    private String code;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "region", nullable = false)
    @Schema(description = "区域（上海，深圳，北京，香港）")
    private String region;
    @Column(name = "popular_flag", nullable = false)
    @Schema(description = "是否常见")
    private Boolean popularFlag;
    @Column(name = "release_date")
    private String releaseDate;
    @Column(name = "scale")
    private String scale;
    @Column(name = "huan_shou_ptg")
    private BigDecimal huanShouPtg;
    @Column(name = "total_shares")
    private BigDecimal totalShares;
    @Column(name = "fenhong")
    private String fenhong;
    @Column(name = "manage_radio")
    private BigDecimal manageRadio;
    @Column(name = "holder_radio")
    private BigDecimal holderRadio;
    @Column(name = "sell_radio")
    private BigDecimal sellRadio;
    @Column(name = "busi_compare_base")
    private String busiCompareBase;
    @Column(name = "follow_index")
    private String followIndex;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改日期")
    private LocalDateTime updateTime;

    @Schema(description = "创建者id")
    private Long createUserId;

    @Schema(description = "更新者id")
    private Long updateUserId;
}
