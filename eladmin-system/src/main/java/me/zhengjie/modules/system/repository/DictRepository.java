package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Dict;

/**
 * @author Zheng Jie
 * @since 2019-04-10
 */
@ApplicationScoped
public class DictRepository extends MyPanacheRepository<Dict> {

}
