package me.vt.modules.maint.repository;

import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.maint.domain.App;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@ApplicationScoped
public class AppRepository extends MyPanacheRepository<App> {
}
