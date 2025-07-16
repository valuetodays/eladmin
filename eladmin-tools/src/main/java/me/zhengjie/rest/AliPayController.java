
package me.zhengjie.rest;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousAccess;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.domain.AlipayConfig;
import me.zhengjie.domain.enums.AliPayStatusEnum;
import me.zhengjie.domain.vo.TradeVo;
import me.zhengjie.service.AliPayService;
import me.zhengjie.utils.AlipayUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Zheng Jie
 * @date 2018-12-31
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
    @Path
    public ResponseEntity<AlipayConfig> queryAliConfig() {
        return new ResponseEntity<>(alipayService.find(), HttpStatus.OK);
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
    public ResponseEntity<String> toPayAsPc(@Valid TradeVo trade) throws Exception {
        AlipayConfig aliPay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsPc(aliPay, trade);
        return ResponseEntity.ok(payUrl);
    }

    @Log("支付宝手机网页支付")
    @Operation(summary = "手机网页支付")
    @POST
    @Path(value = "/toPayAsWeb")
    public ResponseEntity<String> toPayAsWeb(@Valid TradeVo trade) throws Exception {
        AlipayConfig alipay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsWeb(alipay, trade);
        return ResponseEntity.ok(payUrl);
    }

    @ApiIgnore
    @AnonymousGetMapping("/return")
    @Operation(summary = "支付之后跳转的链接")
    public ResponseEntity<String> returnPage(HttpServletRequest request, HttpServletResponse response) {
        AlipayConfig alipay = alipayService.find();
        response.setContentType("text/html;charset=" + alipay.getCharset());
        //内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
            return new ResponseEntity<>("payment successful", HttpStatus.OK);
        } else {
            // 根据业务需要返回数据
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiIgnore
    @Path("/notify")
    @AnonymousAccess
    @Operation(summary = "支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    public Object notify(HttpServletRequest request) {
        AlipayConfig alipay = alipayService.find();
        Map<String, String[]> parameterMap = request.getParameterMap();
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
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
