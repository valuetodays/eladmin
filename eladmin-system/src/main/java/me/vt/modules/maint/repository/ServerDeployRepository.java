package me.vt.modules.maint.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.maint.domain.ServerDeploy;

/**
 * @author zhanghouying
 * @since 2019-08-24
 */
@ApplicationScoped
public class ServerDeployRepository extends MyPanacheRepository<ServerDeploy> {

    /**
     * 根据IP查询
     *
     * @param ip /
     * @return /
     */
    public ServerDeploy findByIp(String ip) {
        return find("ip=?1", ip).firstResult();
    }
}
