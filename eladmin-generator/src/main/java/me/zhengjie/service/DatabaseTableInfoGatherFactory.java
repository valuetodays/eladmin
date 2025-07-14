package me.zhengjie.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.Assert;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-14
 */
public class DatabaseTableInfoGatherFactory {
    public enum DbType {
        MYSQL,
        PGSQL,
    }
    public static DatabaseTableInfoGather choose(DbType dbType) {
        Map<DbType, DatabaseTableInfoGather> map = new HashMap<>();
        map.put(DbType.MYSQL, new MySqlTableInfoGather());
        map.put(DbType.PGSQL, new PgSqlTableInfoGather());

        DatabaseTableInfoGather choosed = map.get(dbType);
        Assert.isTrue(Objects.nonNull(choosed), "no DatabaseTableInfoGather");
        return choosed;
    }
}
