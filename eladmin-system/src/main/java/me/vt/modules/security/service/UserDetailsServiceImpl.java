package me.vt.modules.security.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vt.exception.BadRequestException;
import me.vt.modules.security.component.UserCacheManager;
import me.vt.modules.security.service.dto.AuthorityDto;
import me.vt.modules.security.service.dto.JwtUserDto;
import me.vt.modules.system.service.client.DataService;
import me.vt.modules.system.service.client.RoleService;
import me.vt.modules.system.service.client.UserService;
import me.vt.modules.system.service.dto.UserDto;
import org.apache.commons.lang3.BooleanUtils;

/**
 * @author Zheng Jie
 * @since 2018-11-22
 */
@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class UserDetailsServiceImpl {
    @Inject
    UserService userService;
    @Inject
    RoleService roleService;
    @Inject
    DataService dataService;
    @Inject
    UserCacheManager userCacheManager;

    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (jwtUserDto == null) {
            UserDto user = userService.getLoginData(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (BooleanUtils.isNotTrue(user.getEnabled())) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<AuthorityDto> authorities = roleService.buildPermissions(user);
                // 初始化JwtUserDto
                jwtUserDto = new JwtUserDto(user, dataService.getDeptIds(user.getId()), authorities);
                // 添加缓存数据
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
