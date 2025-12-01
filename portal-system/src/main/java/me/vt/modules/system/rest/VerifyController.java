package me.vt.modules.system.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.vt.domain.vo.EmailVo;
import me.vt.modules.system.service.client.VerifyService;
import me.vt.service.client.EmailService;
import me.vt.utils.enums.CodeBiEnum;
import me.vt.utils.enums.CodeEnum;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * @author Zheng Jie
 * @since 2018-12-26
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
    public Object resetEmail(@QueryParam("email") String email) {
        EmailVo emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return 1;
    }

    @POST
    @Path(value = "/email/resetPass")
    @Operation(summary = "重置密码，发送验证码")
    public Object resetPass(@QueryParam("email") String email) {
        EmailVo emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return 1;
    }

    @POST
    @Path(value = "/validated")
    @Operation(summary = "验证码验证")
    public Object validated(
            /*@RequestParam*/ String email /*,@RequestParam String code,  @RequestParamInteger codeBi*/) {
        // fixme: move to a request obj
        /*@RequestParam*/
        String code = "null";
        /*@RequestParam*/
        Integer codeBi = 111;
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return 1;
    }
}
