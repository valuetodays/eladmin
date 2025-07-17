package me.zhengjie.modules.maint.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Getter
@Setter
public class AppDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "应用名称")
	private String name;

    @Schema(description = "端口")
	private Integer port;

    @Schema(description = "上传目录")
	private String uploadPath;

    @Schema(description = "部署目录")
	private String deployPath;

    @Schema(description = "备份目录")
	private String backupPath;

    @Schema(description = "启动脚本")
	private String startScript;

    @Schema(description = "部署脚本")
	private String deployScript;
}
