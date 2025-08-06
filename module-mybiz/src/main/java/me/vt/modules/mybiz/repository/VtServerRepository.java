package me.vt.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.mybiz.domain.VtServer;

/**
 * @author vt

 * @since 2025-07-11
 **/
@ApplicationScoped
public class VtServerRepository extends MyPanacheRepository<VtServer> {
}
