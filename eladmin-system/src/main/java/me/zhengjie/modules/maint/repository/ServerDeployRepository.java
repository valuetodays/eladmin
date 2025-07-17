package me.zhengjie.modules.maint.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.maint.domain.ServerDeploy;

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
