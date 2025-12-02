package me.vt.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.mybiz.domain.HttpsDomain;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@ApplicationScoped
public class HttpsDomainRepository extends MyPanacheRepository<HttpsDomain> {

}
