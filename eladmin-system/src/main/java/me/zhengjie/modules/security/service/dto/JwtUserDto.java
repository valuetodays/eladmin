package me.zhengjie.modules.security.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@Data
@AllArgsConstructor
public class JwtUserDto {

    @Schema(description = "用户")
    UserDto user;

    @Schema(description = "数据权限")
    List<Long> dataScopes;

    @Schema(description = "角色权限")
    List<AuthorityDto> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(AuthorityDto::getAuthority).collect(Collectors.toSet());
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public boolean isEnabled() {
        return user.getEnabled();
    }
}
