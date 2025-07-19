package me.zhengjie.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserAuthCompositeService;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.utils.CacheKey;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.enums.DataScopeEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @description 数据权限服务实现
 * @since 2020-05-07
 **/
@ApplicationScoped
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    @Inject
    RedisUtils redisUtils;
    @Inject
    RoleService roleService;
    @Inject
    DeptService deptService;
    @Inject
    UserAuthCompositeService userAuthCompositeService;

    /**
     * 用户角色和用户部门改变时需清理缓存
     * @param user /
     * @return /
     */
    @Override
    public List<Long> getDeptIds(User user) {
        String key = CacheKey.DATA_USER + user.getId();
        List<Long> ids = redisUtils.getList(key, Long.class);
        if (CollUtil.isEmpty(ids)) {
            // 用于存储部门id
            Set<Long> deptIds = new HashSet<>();
            // 查询用户角色
            List<RoleSmallDto> roleSet = roleService.findByUsersId(user.getId());
            // 获取对应的部门ID
            for (RoleSmallDto role : roleSet) {
                DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
                switch (Objects.requireNonNull(dataScopeEnum)) {
                    case THIS_LEVEL:
                        deptIds.add(user.getDeptId());
                        break;
                    case CUSTOMIZE:
                        deptIds.addAll(getCustomize(deptIds, role));
                        break;
                    default:
                        return new ArrayList<>();
                }
            }
            ids = new ArrayList<>(deptIds);
            redisUtils.set(key, ids, 1, TimeUnit.DAYS);
        }
        return new ArrayList<>(ids);
    }

    /**
     * 获取自定义的数据权限
     * @param deptIds 部门ID
     * @param role 角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, RoleSmallDto role){
        List<Dept> depts = userAuthCompositeService.findDeptsByRoleIds(List.of(role.getId()));
        for (Dept dept : depts) {
            deptIds.add(dept.getId());
            List<Dept> deptChildren = deptService.findByPid(dept.getId());
            if (CollUtil.isNotEmpty(deptChildren)) {
                deptIds.addAll(deptService.getDeptChildren(deptChildren));
            }
        }
        return deptIds;
    }
}
