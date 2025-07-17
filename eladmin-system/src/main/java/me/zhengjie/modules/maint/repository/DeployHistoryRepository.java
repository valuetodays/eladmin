package me.zhengjie.modules.maint.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.maint.domain.DeployHistory;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@ApplicationScoped
public class DeployHistoryRepository extends MyPanacheRepository<DeployHistory> {
}
