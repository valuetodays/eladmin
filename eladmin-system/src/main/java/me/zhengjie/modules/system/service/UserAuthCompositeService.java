package me.zhengjie.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.RolesDepts;
import me.zhengjie.modules.system.domain.RolesMenus;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UsersJob;
import me.zhengjie.modules.system.domain.UsersRole;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.JobRepository;
import me.zhengjie.modules.system.repository.MenuRepository;
import me.zhengjie.modules.system.repository.RoleRepository;
import me.zhengjie.modules.system.repository.RolesDeptsRepository;
import me.zhengjie.modules.system.repository.RolesMenusRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.repository.UsersJobRepository;
import me.zhengjie.modules.system.repository.UsersRoleRepository;
import me.zhengjie.utils.enums.DataScopeEnum;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    JobRepository jobRepository;
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
    @Inject
    UserRepository userRepository;
    @Inject
    DataService dataService;
    @Inject
    UsersJobRepository usersJobRepository;

    public List<Role> findRolesByUserId(Long userId) {
        List<UsersRole> usersRoles = usersRoleRepository.findByUserId(userId);
        if (CollectionUtils.isEmpty(usersRoles)) {
            return List.of();
        }
        List<Long> roleIds = usersRoles.stream().map(UsersRole::getRoleId).distinct().toList();
        return roleRepository.findAllById(roleIds);
    }

    public List<Job> findJobsByUserId(Long userId) {
        List<UsersJob> usersJobList = usersJobRepository.findByUserIds(List.of(userId));
        if (CollectionUtils.isEmpty(usersJobList)) {
            return List.of();
        }
        List<Long> jobIds = usersJobList.stream().map(UsersJob::getJobId).distinct().toList();
        return jobRepository.findAllById(jobIds);
    }

    public List<Menu> findMenusByRoleIds(Set<Long> roleIds) {
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

    public List<Dept> findDeptsByIds(Collection<Long> deptIds) {
        return deptRepository.findByIds(deptIds);
    }

    public Map<Long, Dept> findDeptsMapByIds(Collection<Long> deptIds) {
        List<Dept> depts = deptRepository.findByIds(deptIds);
        if (CollectionUtils.isEmpty(deptIds)) {
            return Map.of();
        }
        return depts.stream().collect(Collectors.toMap(Dept::getId, e -> e));
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

    public List<Long> findDataScopesByUserId(Long userId) {
        List<Long> dataScopes = dataService.getDeptIds(userId);
        return dataScopes;
    }

    /**
     * 获取数据权限级别
     *
     * @return 级别
     */
    public String findDataScopesTypeByUserId(Long userId) {
        List<Long> dataScopes = findDataScopesByUserId(userId);
        if (CollUtil.isEmpty(dataScopes)) {
            return "";
        }
        return DataScopeEnum.ALL.getValue();
    }

    @Transactional
    public void updateRoleMenus(Long roleId, Set<Long> menuIds) {
        rolesMenusRepository.deleteByRole(roleId);
        List<RolesMenus> toSave = menuIds.stream().map(e -> {
            RolesMenus rm = new RolesMenus();
            rm.setRoleId(roleId);
            rm.setMenuId(e);
            return rm;
        }).toList();
        rolesMenusRepository.persist(toSave);
    }

    /**
     * 根据角色中的部门查询
     *
     * @param deptId /
     * @return /
     */
    public List<User> findUsersByRoleDeptId(Long deptId) {
        List<RolesDepts> rolesDepts = rolesDeptsRepository.findByDeptIds(List.of(deptId));
        List<Long> roleIds = rolesDepts.stream().map(RolesDepts::getRoleId).distinct().toList();
        List<UsersRole> usersRoles = usersRoleRepository.findByRoleIds(roleIds);
        List<Long> userIds = usersRoles.stream().map(UsersRole::getUserId).distinct().toList();
        return userRepository.findAllByIds(userIds);
//    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE " +
//            "u.user_id = r.user_id AND r.role_id = d.role_id AND d.dept_id = ?1 group by u.user_id", nativeQuery = true)
    }


}
