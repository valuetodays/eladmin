package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Dict;

import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @since 2019-04-10
 */
@ApplicationScoped
public class DictRepository extends MyPanacheRepository<Dict> {

    /**
     * 删除
     *
     * @param ids /
     */
    public long deleteByIdIn(Set<Long> ids) {
        return super.deleteAllByIdIn(ids);
    }

    /**
     * 查询
     *
     * @param ids /
     * @return /
     */
    public List<Dict> findByIdIn(Set<Long> ids) {
        return find("id in ?1", ids).list();
    }

}