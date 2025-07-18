package me.zhengjie.modules.system.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.RolesDepts;
import me.zhengjie.modules.system.domain.RolesMenus;
import me.zhengjie.modules.system.domain.UsersRole;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.MenuRepository;
import me.zhengjie.modules.system.repository.RoleRepository;
import me.zhengjie.modules.system.repository.RolesDeptsRepository;
import me.zhengjie.modules.system.repository.RolesMenusRepository;
import me.zhengjie.modules.system.repository.UsersRoleRepository;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@ApplicationScoped
public class UserAuthCompositeService {
    @Inject
    RoleRepository roleRepository;
    @Inject
    MenuRepository menuRepository;
    @Inject
    DeptRepository deptRepository;
    @Inject
    UsersRoleRepository usersRoleRepository;
    @Inject
    RolesMenusRepository rolesMenusRepository;
    @Inject
    RolesDeptsRepository rolesDeptsRepository;

    public List<Role> findRolesByUserId(Long userId) {
        List<UsersRole> usersRoles = usersRoleRepository.findByUserId(userId);
        if (CollectionUtils.isEmpty(usersRoles)) {
            return List.of();
        }
        List<Long> roleIds = usersRoles.stream().map(UsersRole::getRoleId).distinct().toList();
        return roleRepository.findAllById(roleIds);
    }

    public List<Menu> findMenusByRoleIds(List<Long> roleIds) {
        List<RolesMenus> list = rolesMenusRepository.findByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(list)) {
            return List.of();
        }
        return menuRepository.findAllByIds(
            list.stream().map(RolesMenus::getMenuId).collect(Collectors.toSet())
        );
    }

    public List<Dept> findDeptsByRoleIds(List<Long> roleIds) {
        List<RolesDepts> list = rolesDeptsRepository.findByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(list)) {
            return List.of();
        }
        return deptRepository.findAllByIds(
            list.stream().map(RolesDepts::getDeptId).collect(Collectors.toSet())
        );
    }

    public Dept findDeptById(Long deptId) {
        return deptRepository.findById(deptId);
    }

    public List<Menu> findMenusByRoleIdsAndTypeNot(Set<Long> roleIds, int i) {
        List<RolesMenus> list = rolesMenusRepository.findByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(list)) {
            return List.of();
        }
        List<Long> menuIds = list.stream().map(RolesMenus::getMenuId).toList();
        if (CollectionUtils.isEmpty(menuIds)) {
            return List.of();
        }
        return menuRepository.findByIdsAndTypeNotAndSortable(menuIds, i);
    }
}
