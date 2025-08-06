package me.vt.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.domain.AlipayConfig;

/**
 * @author Zheng Jie
 * @since 2018-12-31
 */
@ApplicationScoped
public class AliPayRepository extends MyPanacheRepository<AlipayConfig> {
}
