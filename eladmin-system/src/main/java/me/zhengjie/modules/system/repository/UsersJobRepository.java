package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.UsersJob;

import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-19
 */
@ApplicationScoped
public class UsersJobRepository extends MyPanacheRepository<UsersJob> {
    public List<UsersJob> findByUserIds(List<Long> userIds) {
        return find("userId in ?1", userIds).list();
    }

    public List<UsersJob> findByJobIds(List<Long> jobIds) {
        return find("jobId in ?1", jobIds).list();
    }
}
