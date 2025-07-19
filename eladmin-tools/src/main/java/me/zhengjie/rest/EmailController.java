package me.zhengjie.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.EmailConfig;
import me.zhengjie.domain.vo.EmailVo;
import me.zhengjie.service.EmailService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * 发送邮件
 * @author 郑杰
 * @since 2018/09/28 6:55:53
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("api/email")
@Tag(name = "工具：邮件管理")
public class EmailController {

    @Inject
    EmailService emailService;

    @GET
    @Path("")
    public EmailConfig queryEmailConfig() {
        return emailService.find();
    }

    @Log("配置邮件")
    @POST
    @Path("updateEmailConfig")
    @Operation(summary = "配置邮件")
    public Object updateEmailConfig(@Valid EmailConfig emailConfig) throws Exception {
        emailService.config(emailConfig,emailService.find());
        return 1;
    }

    @Log("发送邮件")
    @POST
    @Path("sendEmail")
    @Operation(summary = "发送邮件")
    public Object sendEmail(@Valid EmailVo emailVo) {
        emailService.send(emailVo,emailService.find());
        return 1;
    }
}
