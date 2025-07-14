package me.zhengjie.service;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-14
 */
public class MySqlTableInfoGather implements DatabaseTableInfoGather {
    @Override
    public String getSqlForTables() {
        return "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables "
               + "where table_schema = (select database()) "
               + "order by create_time desc";
    }

    @Override
    public String getSqlForTablesForQuery() {
        return "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables "
               + "where table_schema = (select database()) "
               + "and table_name like :table "
               + "order by create_time desc";
    }

    @Override
    public String getSqlForColumns(String name) {
        return "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                     "where table_name = ? and table_schema = (select database()) order by ordinal_position";
    }
}
