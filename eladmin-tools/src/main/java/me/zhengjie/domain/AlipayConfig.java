
package me.zhengjie.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 支付宝配置类
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Data
@Entity
@Table(name = "tool_alipay_config")
public class AlipayConfig implements Serializable {

    @Id
    @Column(name = "config_id")
    @Schema(description = "ID", hidden = true)
    private Long id;

    @NotBlank
    @Schema(description = "应用ID")
    private String appId;

    @NotBlank
    @Schema(description = "商户私钥")
    private String privateKey;

    @NotBlank
    @Schema(description = "支付宝公钥")
    private String publicKey;

    @Schema(description = "签名方式")
    private String signType="RSA2";

    @Column(name = "gateway_url")
    @Schema(description = "支付宝开放安全地址", hidden = true)
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    @Schema(description = "编码", hidden = true)
    private String charset= "utf-8";

    @NotBlank
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @NotBlank
    @Schema(description = "订单完成后返回的页面")
    private String returnUrl;

    @Schema(description = "类型")
    private String format="JSON";

    @NotBlank
    @Schema(description = "商户号")
    private String sysServiceProviderId;

}
