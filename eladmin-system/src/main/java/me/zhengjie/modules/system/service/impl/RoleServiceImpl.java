package me.zhengjie.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.valuetodays.quarkus.commons.QueryPart;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.security.service.UserCacheManager;
import me.zhengjie.modules.security.service.dto.AuthorityDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.RoleRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserAuthCompositeService;
import me.zhengjie.modules.system.service.dto.RoleDto;
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.mapstruct.DeptMapper;
import me.zhengjie.modules.system.service.mapstruct.MenuMapper;
import me.zhengjie.modules.system.service.mapstruct.RoleMapper;
import me.zhengjie.modules.system.service.mapstruct.RoleSmallMapper;
import me.zhengjie.utils.CacheKey;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.ValidationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @since 2018-12-03
 */
@ApplicationScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    RoleRepository roleRepository;
    @Inject
    RoleMapper roleMapper;
    @Inject
    MenuMapper menuMapper;
    @Inject
    DeptMapper deptMapper;
    @Inject
    RoleSmallMapper roleSmallMapper;
    @Inject
    RedisUtils redisUtils;
    @Inject
    UserRepository userRepository;
    @Inject
    UserCacheManager userCacheManager;
    @Inject
    UserAuthCompositeService userAuthCompositeService;

    @Override
    public List<RoleDto> queryAll() {
        Sort sort = Sort.ascending("level");
        List<Role> list = roleRepository.findAll(sort).list();
        return roleMapper.toDto(list);
    }

    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    @Override
    public PageResult<RoleDto> queryAll(RoleQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, Role.class);
        PanacheQuery<Role> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = roleRepository.findAll(sort);
        } else {
            panacheQuery = roleRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }
        List<Role> list = panacheQuery.list();
        long count = panacheQuery.count();
        List<RoleDto> dtoList = roleMapper.toDto(list);
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (RoleDto roleDto : dtoList) {
                List<Long> roleIds = List.of(roleDto.getId());
                List<Menu> menusByRoleIds = userAuthCompositeService.findMenusByRoleIds(new HashSet<>(roleIds));
                roleDto.setMenus(new HashSet<>(menuMapper.toDto(menusByRoleIds)));
                List<Dept> deptsByRoleIds = userAuthCompositeService.findDeptsByRoleIds(roleIds);
                roleDto.setDepts(new HashSet<>(deptMapper.toDto(deptsByRoleIds)));
            }
        }
        return PageUtil.toPage(dtoList, count);
    }

    @Override
    public RoleDto findById(long id) {
        String key = CacheKey.ROLE_ID + id;
        Role role = redisUtils.get(key, Role.class);
        if (role == null) {
            role = roleRepository.findById(id);
            ValidationUtil.isNull(role.getId(), "Role", "id", id);
            redisUtils.set(key, role, 1, TimeUnit.DAYS);
        }
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(Role resources) {
        if (roleRepository.findByName(resources.getName()) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        roleRepository.save(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(Role resources) {
        Role role = roleRepository.findById(resources.getId());
        ValidationUtil.isNull(role.getId(), "Role", "id", resources.getId());

        Role role1 = roleRepository.findByName(resources.getName());

        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
// fixme        role.setDepts(resources.getDepts());
        role.setLevel(resources.getLevel());
        roleRepository.save(role);
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    @Override
    @Transactional
    public void updateMenu(Set<Long> menuIds, RoleDto roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        List<User> users = userRepository.findByRoleId(role.getId());
        // 更新菜单
        userAuthCompositeService.updateRoleMenus(role.getId(), menuIds);
        delCaches(role.getId(), users);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void untiedMenu(Long menuId) {
        // 更新菜单
        roleRepository.untiedMenu(menuId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 更新相关缓存
            delCaches(id, null);
        }
        roleRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<RoleSmallDto> findByUsersId(Long userId) {
        String key = CacheKey.ROLE_USER + userId;
        List<RoleSmallDto> roles = redisUtils.getList(key, RoleSmallDto.class);
        if (CollUtil.isEmpty(roles)) {
            List<Role> rolesByUserId = userAuthCompositeService.findRolesByUserId(userId);
            roles = roleSmallMapper.toDto(new ArrayList<>(rolesByUserId));
            redisUtils.set(key, roles, 1, TimeUnit.DAYS);
        }
        return roles;
    }

    @Override
    public Integer findByRoles(Set<Role> roles) {
        if (roles.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        Set<RoleDto> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDto::getLevel).toList());
    }

    @Override
    public List<AuthorityDto> buildPermissions(UserDto user) {
        String key = CacheKey.ROLE_AUTH + user.getId();
        List<AuthorityDto> authorityDtos = redisUtils.getList(key, AuthorityDto.class);
        if (CollUtil.isEmpty(authorityDtos)) {
            Set<String> permissions = new HashSet<>();
            // 如果是管理员直接返回
            if (user.getIsAdmin()) {
                permissions.add("admin");
                return permissions.stream().map(AuthorityDto::new)
                        .collect(Collectors.toList());
            }
            List<Role> roles = userAuthCompositeService.findRolesByUserId(user.getId());
            final List<Long> roleIds = roles.stream().map(Role::getId).distinct().toList();
            List<Menu> menus = userAuthCompositeService.findMenusByRoleIds(new HashSet<>(roleIds));
            permissions = menus.stream()
                .map(Menu::getPermission)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
            authorityDtos = permissions.stream()
                .map(AuthorityDto::new)
                .collect(Collectors.toList());
            redisUtils.set(key, authorityDtos, 1, TimeUnit.HOURS);
        }
        return authorityDtos;
    }

    @Override
    public File download(List<RoleDto> roles) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDto role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        return FileUtil.downloadExcel(list);
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userRepository.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<Role> findInMenuId(List<Long> menuIds) {
        return roleRepository.findInMenuId(menuIds);
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userRepository.findByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheManager.cleanUserCache(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATA_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_USER, userIds);
        }
        redisUtils.del(CacheKey.ROLE_ID + id);
    }
}
