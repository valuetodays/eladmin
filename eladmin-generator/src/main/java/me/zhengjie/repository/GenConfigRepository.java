package me.zhengjie.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.GenConfig;

/**
 * @author Zheng Jie
 * @since 2019-01-14
 */
@ApplicationScoped
public class GenConfigRepository extends MyPanacheRepository<GenConfig> {

    /**
     * 查询表配置
     * @param tableName 表名
     * @return /
     */
    public GenConfig findByTableName(String tableName) {
        return find("tableName = ?1", tableName).firstResult();
    }
}
