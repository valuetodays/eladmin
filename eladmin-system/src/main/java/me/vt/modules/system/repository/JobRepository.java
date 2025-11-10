package me.vt.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.system.domain.Job;

/**
* @author Zheng Jie
 * @since 2019-03-29
*/
@ApplicationScoped
public class JobRepository extends MyPanacheRepository<Job> {

    /**
     * 根据名称查询
     * @param name 名称
     * @return /
     */
    public Job findByName(String name) {
        return find("name = ?1", name).firstResult();
    }

}
