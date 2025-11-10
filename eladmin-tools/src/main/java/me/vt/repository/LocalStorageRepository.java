package me.vt.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.domain.LocalStorage;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@ApplicationScoped
public class LocalStorageRepository extends MyPanacheRepository<LocalStorage> {
}
