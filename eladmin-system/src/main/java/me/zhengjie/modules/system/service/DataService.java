package me.zhengjie.modules.system.service;

import java.util.List;

/**
 * 数据权限服务类
 * @author Zheng Jie
 * @since 2020-05-07
 */
public interface DataService {

    /**
     * 获取数据权限
     * @param userId /
     * @return /
     */
    List<Long> getDeptIds(Long userId);
}
