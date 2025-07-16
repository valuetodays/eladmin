
package me.zhengjie.modules.system.domain.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 * @author Zheng Jie
 * @date 2019年7月11日13:59:49
 */
@Data
public class UserPassVo {

    @Schema(description = "旧密码")
    private String oldPass;

    @Schema(description = "新密码")
    private String newPass;
}
