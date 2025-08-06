package me.vt.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.system.domain.Dept;
import me.vt.modules.system.domain.Job;
import me.vt.modules.system.domain.Menu;
import me.vt.modules.system.domain.Role;
import me.vt.modules.system.domain.RolesDepts;
import me.vt.modules.system.domain.RolesMenus;
import me.vt.modules.system.domain.User;
import me.vt.modules.system.domain.UsersJob;
import me.vt.modules.system.domain.UsersRole;
import me.vt.modules.system.repository.DeptRepository;
import me.vt.modules.system.repository.JobRepository;
import me.vt.modules.system.repository.MenuRepository;
import me.vt.modules.system.repository.RoleRepository;
import me.vt.modules.system.repository.RolesDeptsRepository;
import me.vt.modules.system.repository.RolesMenusRepository;
import me.vt.modules.system.repository.UserRepository;
import me.vt.modules.system.repository.UsersJobRepository;
import me.vt.modules.system.repository.UsersRoleRepository;
import me.vt.utils.enums.DataScopeEnum;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
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
@Slf4j
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
        Set<Long> menuIds = list.stream().map(RolesMenus::getMenuId).collect(Collectors.toSet());
        log.info("menuIds={}", menuIds);
        return menuRepository.findAllByIds(menuIds);
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
        return deptRepository.findByIds(new HashSet<>(deptIds));
    }

    public Map<Long, Dept> findDeptsMapByIds(Collection<Long> deptIds) {
        List<Dept> depts = findDeptsByIds(deptIds);
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
        Set<Long> menuIds = list.stream().map(RolesMenus::getMenuId).distinct().collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(menuIds)) {
            return List.of();
        }
        log.info("#2 menuIds={}", menuIds);
        return menuRepository.findByIdsAndTypeNotAndSortable(menuIds, i);
    }

    public List<Long> findDataScopesByUserId(Long userId) {
        return dataService.getDeptIds(userId);
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
        rolesMenusRepository.deleteByRoleId(roleId);
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
        return userRepository.findAllByIds(new HashSet<>(userIds));
//    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE " +
//            "u.user_id = r.user_id AND r.role_id = d.role_id AND d.dept_id = ?1 group by u.user_id", nativeQuery = true)
    }


    public List<User> findUsersByMenuId(Long menuId) {
        //    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles ur, sys_roles_menus rm WHERE\n" +
//            "u.user_id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = ?1 group by u.user_id", nativeQuery = true)
        List<UsersRole> usersRoles = findUsersRolesByMenuIds(List.of(menuId));
        if (CollectionUtils.isEmpty(usersRoles)) {
            return List.of();
        }
        List<Long> userIds = usersRoles.stream().map(UsersRole::getUserId).distinct().toList();
        return userRepository.findAllByIds(userIds);
    }

    public List<User> findUsersByRoleId(Long roleId) {
        //    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r WHERE" +
//            " u.user_id = r.user_id AND r.role_id = ?1", nativeQuery = true)
        List<UsersRole> usersRoles = usersRoleRepository.findByRoleIds(List.of(roleId));
        if (CollectionUtils.isEmpty(usersRoles)) {
            return List.of();
        }
        List<Long> userIds = usersRoles.stream().map(UsersRole::getUserId).distinct().toList();
        return userRepository.findAllByIds(userIds);
    }

    private List<UsersRole> findUsersRolesByMenuIds(List<Long> menuId) {
        List<RolesMenus> rolesMenus = rolesMenusRepository.findByMenuIds(menuId);
        if (CollectionUtils.isEmpty(rolesMenus)) {
            return List.of();
        }
        List<Long> roleIds = rolesMenus.stream().map(RolesMenus::getRoleId).distinct().toList();
        return usersRoleRepository.findByRoleIds(roleIds);
    }

    public List<Role> findRolesByMenuId(List<Long> menuId) {
        List<UsersRole> usersRoles = findUsersRolesByMenuIds(menuId);
        if (CollectionUtils.isEmpty(usersRoles)) {
            return List.of();
        }
        List<Long> userIds = usersRoles.stream().map(UsersRole::getUserId).distinct().toList();
        return roleRepository.findAllByIds(userIds);
    }

    @Transactional
    public void deleteRolesLinkByMenuId(Long menuId) {
        rolesMenusRepository.deleteByMenuId(menuId);
    }


    public int countRolesByDeptIds(Set<Long> deptIds) {
        //    @Query(value = "select count(1) from sys_role r, sys_roles_depts d where " +
//            "r.role_id = d.role_id and d.dept_id in ?1",nativeQuery = true)
        List<RolesDepts> rolesDepts = rolesDeptsRepository.findByDeptIds(deptIds);
        if (CollectionUtils.isEmpty(rolesDepts)) {
            return 0;
        }
        List<Long> roleIds = rolesDepts.stream().map(RolesDepts::getRoleId).distinct().toList();
        List<Role> roles = roleRepository.findAllByIds(roleIds);
        return CollectionUtils.size(roles);
    }

    public int countUsersByRoleIds(Set<Long> roleIds) {
        //    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE " +
//            "u.user_id = r.user_id AND r.role_id in ?1", nativeQuery = true)
        List<UsersRole> usersRoles = usersRoleRepository.findByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(usersRoles)) {
            return 0;
        }
        List<Long> userIds = usersRoles.stream().map(UsersRole::getUserId).distinct().toList();
        List<User> users = userRepository.findAllByIds(userIds);
        return CollectionUtils.size(users);
    }

    public int countUsersByJobIds(Set<Long> jobIds) {
//    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.user_id = j.user_id AND j.job_id IN ?1",
//    nativeQuery = true)
        List<UsersJob> usersJobs = usersJobRepository.findByJobIds(jobIds);
        if (CollectionUtils.isEmpty(usersJobs)) {
            return 0;
        }
        List<Long> userIds = usersJobs.stream().map(UsersJob::getUserId).distinct().toList();
        List<User> users = userRepository.findAllByIds(userIds);
        return CollectionUtils.size(users);
    }
}
