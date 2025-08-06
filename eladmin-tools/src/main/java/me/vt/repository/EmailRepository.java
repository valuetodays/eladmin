package me.vt.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.domain.EmailConfig;

/**
 * @author Zheng Jie
 * @since 2018-12-26
 */
@ApplicationScoped
public class EmailRepository extends MyPanacheRepository<EmailConfig> {
}
