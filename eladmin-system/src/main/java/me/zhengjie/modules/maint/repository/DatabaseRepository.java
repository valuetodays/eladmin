package me.zhengjie.modules.maint.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import me.zhengjie.modules.maint.domain.Database;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@ApplicationScoped
public class DatabaseRepository implements PanacheRepositoryBase<Database, String> {
}
