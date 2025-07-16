
package me.zhengjie.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.EmailConfig;
import me.zhengjie.domain.vo.EmailVo;
import me.zhengjie.service.EmailService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 发送邮件
 * @author 郑杰
 * @date 2018/09/28 6:55:53
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
    @Path
    public ResponseEntity<EmailConfig> queryEmailConfig(){
        return new ResponseEntity<>(emailService.find(),HttpStatus.OK);
    }

    @Log("配置邮件")
    @PUT
    @Path("")
    @Operation(summary = "配置邮件")
    public Object updateEmailConfig(@Valid EmailConfig emailConfig) throws Exception {
        emailService.config(emailConfig,emailService.find());
        return 1;
    }

    @Log("发送邮件")
    @POST
    @Path("")
    @Operation(summary = "发送邮件")
    public Object sendEmail(@Valid EmailVo emailVo) {
        emailService.send(emailVo,emailService.find());
        return 1;
    }
}
