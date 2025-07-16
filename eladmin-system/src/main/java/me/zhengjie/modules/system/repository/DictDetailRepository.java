
package me.zhengjie.modules.system.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.DictDetail;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@ApplicationScoped
public class DictDetailRepository extends MyPanacheRepository<DictDetail> {

    /**
     * 根据字典名称查询
     * @param name /
     * @return /
     */
    public List<DictDetail> findByDictName(String name) {
        return find("dictName =?1", name).list();
    }
}