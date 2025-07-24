package me.zhengjie.modules.mybiz.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.mybiz.domain.NationCode;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@ApplicationScoped
public class NationCodeRepository extends MyPanacheRepository<NationCode> {
}
