package me.vt.modules.maint.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.maint.domain.DeployHistory;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@ApplicationScoped
public class DeployHistoryRepository extends MyPanacheRepository<DeployHistory> {
}
