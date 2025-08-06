package me.vt.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.mybiz.domain.NationCode;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@ApplicationScoped
public class NationCodeRepository extends MyPanacheRepository<NationCode> {
}
