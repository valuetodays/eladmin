package me.zhengjie.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 交易详情，按需应该存入数据库，这里存入数据库，仅供临时测试
 * @author Zheng Jie
 * @since 2018-12-31
 */
@Data
public class TradeVo {

    @NotBlank
    @Schema(description = "商品描述")
    private String body;

    @NotBlank
    @Schema(description = "商品名称")
    private String subject;

    @Schema(description = "商户订单号", hidden = true)
    private String outTradeNo;

    @Schema(description = "第三方订单号", hidden = true)
    private String tradeNo;

    @NotBlank
    @Schema(description = "价格")
    private String totalAmount;

    @Schema(description = "订单状态,已支付，未支付，作废", hidden = true)
    private String state;

    @Schema(description = "创建时间", hidden = true)
    private Timestamp createTime;

    @Schema(description = "作废时间", hidden = true)
    private Date cancelTime;
}
