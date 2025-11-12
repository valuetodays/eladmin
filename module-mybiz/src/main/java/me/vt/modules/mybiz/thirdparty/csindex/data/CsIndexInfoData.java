package me.vt.modules.mybiz.thirdparty.csindex.data;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-12
 */
@Data
public class CsIndexInfoData implements Serializable {
    private String indexFullNameCn;
    private String indexShortNameCn;
    private String indexFullNameEn;
    private String indexShortNameEn;
    private String indexCode;
    private String ric;
    private String bloombergid;
    @Schema(description = "基日 yyy-MM-dd")
    private String basicDate;
    @Schema(description = "基点")
    private BigDecimal basicIndex;
    @Schema(description = "发布日期 yyyy-MM-dd")
    private String publishDate;
    private String publishChannelCn;
    private String publishChannelEn;
    private String consNumber;
    private String adjFreqCn;
    private String adjFreqEn;
    private String currencyCn;
    private String currencyEn;
    private String indexType; // stock
    private String indexCnDesc;
    private String indexEnDesc;
    private String weightingType;
    private String weightingTypeEn;
    private String ifWeightCapped;
    private String ifWeightCappedEn;
    private String indexCompliance; // 指数合规
    private boolean ifProtect;
    private String protectStartDate;
    private String protectEndDate;
    private String protectCnDesc;
}
