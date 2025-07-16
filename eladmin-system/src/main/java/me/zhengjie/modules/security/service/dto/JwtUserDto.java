
package me.zhengjie.modules.security.service.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

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

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getUsername();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
