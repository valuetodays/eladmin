package me.vt.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.system.domain.DictDetail;

/**
* @author Zheng Jie
 * @since 2019-04-10
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
