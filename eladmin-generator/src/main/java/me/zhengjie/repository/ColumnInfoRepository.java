package me.zhengjie.repository;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.ColumnInfo;

import java.util.List;

/**
 * @author Zheng Jie
 * @since 2019-01-14
 */
@ApplicationScoped
public class ColumnInfoRepository extends MyPanacheRepository<ColumnInfo> {

    /**
     * 查询表信息
     * @param tableName 表格名
     * @return 表信息
     */
    public List<ColumnInfo> findByTableNameOrderByIdAsc(String tableName) {
        return find("tableName = ?1", Sort.ascending("id"), tableName).list();
    }
}
