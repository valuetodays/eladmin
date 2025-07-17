package me.zhengjie.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousAccess;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.domain.AlipayConfig;
import me.zhengjie.domain.vo.TradeVo;
import me.zhengjie.service.AliPayService;
import me.zhengjie.utils.AlipayUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * @author Zheng Jie
 * @since 2018-12-31
 */
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/aliPay")
@Tag(name = "工具：支付宝管理")
public class AliPayController {

    @Inject
    AlipayUtils alipayUtils;
    @Inject
    AliPayService alipayService;

    @GET
    @Path("")
    public AlipayConfig queryAliConfig() {
        return alipayService.find();
    }

    @Log("配置支付宝")
    @Operation(summary = "配置支付宝")
    @PUT
    @Path("")
    public Object updateAliPayConfig(@Valid AlipayConfig alipayConfig) {
        alipayService.config(alipayConfig);
        return 1;
    }

    @Log("支付宝PC网页支付")
    @Operation(summary = "PC网页支付")
    @POST
    @Path(value = "/toPayAsPC")
    public String toPayAsPc(@Valid TradeVo trade) throws Exception {
        AlipayConfig aliPay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsPc(aliPay, trade);
        return payUrl;
    }

    @Log("支付宝手机网页支付")
    @Operation(summary = "手机网页支付")
    @POST
    @Path(value = "/toPayAsWeb")
    public String toPayAsWeb(@Valid TradeVo trade) throws Exception {
        AlipayConfig alipay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsWeb(alipay, trade);
        return payUrl;
    }

    @AnonymousGetMapping("/return")
    @Operation(summary = "支付之后跳转的链接")
    public String returnPage(/*HttpServletRequest request, HttpServletResponse response*/) {
        AlipayConfig alipay = alipayService.find();
//        response.setContentType("text/html;charset=" + alipay.getCharset());
        //内容验签，防止黑客篡改参数
        //    if (alipayUtils.rsaCheck(request, alipay)) {
            //商户订单号
//            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            支付宝交易号
//            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
        return "payment successful";
        //    } else {
            // 根据业务需要返回数据
        //        return "fail";
        //    }
    }

    @Path("/notify")
    @AnonymousAccess
    @Operation(summary = "支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    public Object notifyA(/*HttpServletRequest request*/) {
        AlipayConfig alipay = alipayService.find();
       /* Map<String, String[]> parameterMap = request.getParameterMap();
        //内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //验证
            if (tradeStatus.equals(AliPayStatusEnum.SUCCESS.getValue()) || tradeStatus.equals(AliPayStatusEnum.FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
            }
            return 1;
        }*/
        return 1;
    }
}
