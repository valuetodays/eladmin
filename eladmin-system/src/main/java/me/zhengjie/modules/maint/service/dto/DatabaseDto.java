
package me.zhengjie.modules.maint.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Getter
@Setter
public class DatabaseDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "数据库名称")
    private String name;

    @Schema(description = "数据库连接地址")
    private String jdbcUrl;

    @Schema(description = "数据库密码")
    private String pwd;

    @Schema(description = "用户名")
    private String userName;
}
