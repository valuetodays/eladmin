package me.zhengjie.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.LocalStorage;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@ApplicationScoped
public class LocalStorageRepository extends MyPanacheRepository<LocalStorage> {
}