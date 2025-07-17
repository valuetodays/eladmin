package me.zhengjie.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.AlipayConfig;

/**
 * @author Zheng Jie
 * @since 2018-12-31
 */
@ApplicationScoped
public class AliPayRepository extends MyPanacheRepository<AlipayConfig> {
}
