package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Job;

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