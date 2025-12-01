package me.vt.modules.maint.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.maint.domain.Deploy;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@ApplicationScoped
public class DeployRepository extends MyPanacheRepository<Deploy> {
}
