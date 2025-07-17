package me.zhengjie.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.EmailConfig;

/**
 * @author Zheng Jie
 * @since 2018-12-26
 */
@ApplicationScoped
public class EmailRepository extends MyPanacheRepository<EmailConfig> {
}
