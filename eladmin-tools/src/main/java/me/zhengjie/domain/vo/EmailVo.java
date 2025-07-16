
package me.zhengjie.domain.vo;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送邮件时，接收参数的类
 * @author 郑杰
 * @date 2018/09/28 12:02:14
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
