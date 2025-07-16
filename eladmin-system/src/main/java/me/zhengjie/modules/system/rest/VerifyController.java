package me.zhengjie.modules.system.rest;

import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.vo.EmailVo;
import me.zhengjie.modules.system.service.VerifyService;
import me.zhengjie.service.EmailService;
import me.zhengjie.utils.enums.CodeBiEnum;
import me.zhengjie.utils.enums.CodeEnum;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-26
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/code")
@Tag(name = "系统：验证码管理")
public class VerifyController {

    @Inject
    VerifyService verificationCodeService;
    @Inject
    EmailService emailService;

    @POST
    @Path(value = "/resetEmail")
    @Operation(summary = "重置邮箱，发送验证码")
    public Object resetEmail(@RequestParam String email) {
        EmailVo emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo,emailService.find());
        return 1;
    }

    @POST
    @Path(value = "/email/resetPass")
    @Operation(summary = "重置密码，发送验证码")
    public Object resetPass(@RequestParam String email) {
        EmailVo emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo,emailService.find());
        return 1;
    }

    @GET
    @Path(value = "/validated")
    @Operation(summary = "验证码验证")
    public Object validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)){
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email ,code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email ,code);
                break;
            default:
                break;
        }
        return 1;
    }
}
