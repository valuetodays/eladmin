package me.zhengjie.domain.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

/**
 * 发送邮件时，接收参数的类
 * @author 郑杰
 * @since 2018/09/28 12:02:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVo {

    @NotEmpty
    @Schema(description = "收件人")
    private List<String> tos;

    @NotBlank
    @Schema(description = "主题")
    private String subject;

    @NotBlank
    @Schema(description = "内容")
    private String content;
}
