
package me.zhengjie.modules.system.repository;

import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Dict;

/**
 * @author Zheng Jie
 * @date 2019-04-10
 */
@ApplicationScoped
public class DictRepository extends MyPanacheRepository<Dict> {

    /**
     * 删除
     *
     * @param ids /
     */
    public long deleteByIdIn(Set<Long> ids) {
        return delete("where id in ?1", ids);
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