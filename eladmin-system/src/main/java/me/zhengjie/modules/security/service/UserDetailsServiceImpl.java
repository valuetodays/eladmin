
package me.zhengjie.modules.security.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.AuthorityDto;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@Slf4j
@RequiredArgsConstructor
@ApplicationScoped("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Inject
    UserService userService;
    @Inject
    RoleService roleService;
    @Inject
    DataService dataService;
    @Inject
    UserCacheManager userCacheManager;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if(jwtUserDto == null){
            UserDto user = userService.getLoginData(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<AuthorityDto> authorities = roleService.buildPermissions(user);
                // 初始化JwtUserDto
                jwtUserDto = new JwtUserDto(user, dataService.getDeptIds(user), authorities);
                // 添加缓存数据
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
