
package me.zhengjie.modules.mybiz.repository;

import me.zhengjie.modules.mybiz.domain.VtServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author vt
 * @website https://eladmin.vip
 * @date 2025-07-11
 **/
public interface VtServerRepository extends JpaRepository<VtServer, Long>, JpaSpecificationExecutor<VtServer> {
}