package me.zhengjie.modules.maint.service.dto;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Getter
@Setter
public class DeployDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "应用")
	private AppDto app;

    @Schema(description = "服务器")
	private Set<ServerDeployDto> deploys;

    @Schema(description = "服务器名称")
	private String servers;

    @Schema(description = "服务状态")
	private String status;

	public String getServers() {
		if(CollectionUtil.isNotEmpty(deploys)){
			return deploys.stream().map(ServerDeployDto::getName).collect(Collectors.joining(","));
		}
		return servers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeployDto deployDto = (DeployDto) o;
		return Objects.equals(id, deployDto.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
