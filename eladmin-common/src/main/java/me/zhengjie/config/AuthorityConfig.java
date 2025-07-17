package me.zhengjie.config;

import cn.vt.auth.AuthUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.zhengjie.utils.SecurityUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zheng Jie
 */
@ApplicationScoped
public class AuthorityConfig {
    @Inject
    SecurityUtils securityUtils;

    /**
     * 判断接口是否有权限
     * @param permissions 权限
     * @return /
     */
    public Boolean check(String ...permissions){
        // 获取当前用户的所有权限
        AuthUser currentUser = securityUtils.getCurrentUser();
        List<String> elPermissions = List.of(currentUser.getEmail());
//  fixme:      List<String> elPermissions = currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
