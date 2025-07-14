package me.zhengjie.modules.mybiz.repository;

import me.zhengjie.modules.mybiz.domain.NationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author vt
* @since 2025-07-14 22:15
**/
public interface NationCodeRepository extends JpaRepository<NationCode, Long>, JpaSpecificationExecutor<NationCode> {
}