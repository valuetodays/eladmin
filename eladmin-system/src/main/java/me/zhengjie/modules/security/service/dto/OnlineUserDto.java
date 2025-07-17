package me.zhengjie.modules.security.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 * @author Zheng Jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDto {

    @Schema(description = "Token编号")
    private String uid;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "岗位")
    private String dept;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "token")
    private String key;

    @Schema(description = "登录时间")
    private Date loginTime;
}
