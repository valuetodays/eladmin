package me.vt.modules.mybiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ll.vt.quarkus.commons.base.jpa.JpaCrudLongIdBasePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "f_stock_info")
public class StockInfoPersist extends JpaCrudLongIdBasePersist {

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
    @Column(name = "t0_flag", nullable = false)
    @Schema(description = "是否t0")
    private Boolean t0Flag;
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
    @Column(name = "fenhong_times")
    private Integer fenhongTimes;
    @Column(name = "fenhong_total_amt_per_share")
    private BigDecimal fenhongTotalAmtPerShare;
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

}
