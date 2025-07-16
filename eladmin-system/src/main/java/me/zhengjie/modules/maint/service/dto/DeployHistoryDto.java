
package me.zhengjie.modules.maint.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Data
public class DeployHistoryDto implements Serializable {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "部署IP")
    private String ip;

    @Schema(description = "部署时间")
	private Timestamp deployDate;

    @Schema(description = "部署人员")
	private String deployUser;

    @Schema(description = "部署编号")
	private Long deployId;
}
