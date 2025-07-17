package me.zhengjie.modules.security.service.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUserDto {

    @Schema(description = "用户")
    @Inject
    UserDto user;

    @Schema(description = "数据权限")
    @Inject
    List<Long> dataScopes;

    @Schema(description = "角色权限")
    @Inject
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

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return user.getEnabled();
    }
}
