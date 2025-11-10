package me.vt.service.kits.database;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-14
 */
public interface DatabaseTableInfoGather {
    String getSqlForTables();
    String getSqlForTablesForQuery();
    String getSqlForColumns(String name);
}
