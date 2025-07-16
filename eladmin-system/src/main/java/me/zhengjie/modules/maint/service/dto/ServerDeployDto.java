
package me.zhengjie.modules.maint.service.dto;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Getter
@Setter
public class ServerDeployDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
	private String name;

    @Schema(description = "IP")
	private String ip;

    @Schema(description = "端口")
	private Integer port;

    @Schema(description = "账号")
	private String account;

    @Schema(description = "密码")
	private String password;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ServerDeployDto that = (ServerDeployDto) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
