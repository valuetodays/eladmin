package me.zhengjie.service;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-14
 */
public class PgSqlTableInfoGather implements DatabaseTableInfoGather {
    @Override
    public String getSqlForTables() {
        return "SELECT \n"
               + "    c.relname AS table_name,\n"
               + "    obj_description(c.oid) AS table_comment\n"
               + "FROM pg_class c\n"
               + "JOIN  pg_namespace n ON n.oid = c.relnamespace\n"
               + "WHERE  c.relkind = 'r'  AND n.nspname = 'public' \n"
               + "ORDER BY  c.oid DESC ";
    }

    @Override
    public String getSqlForTablesForQuery() {
        return "SELECT \n"
                          + "    c.relname AS table_name,\n"
                          + "    null as create_time, null as engine, null as table_collation, "
                          + "    obj_description(c.oid) AS table_comment\n"
                          + "FROM  pg_class c\n"
                          + "JOIN  pg_namespace n ON n.oid = c.relnamespace\n"
                          + "WHERE \n"
                          + "    c.relkind = 'r' \n"
                          + "    AND n.nspname = 'public' "
                          + "    AND c.relname like :table "
                          + "ORDER BY  c.oid DESC ";
    }

    @Override
    public String getSqlForColumns(String name) {
        return "SELECT \n"
               + "    column_name,\n"
               + "    is_nullable,\n"
               + "    data_type,\n"
               + "    col_description(format('%s.%s', table_schema, table_name)::regclass::oid, ordinal_position) AS column_comment,\n"
               + "    '' AS column_key, "
               + "    CASE \n"
               + "        WHEN is_identity = 'YES' THEN 'auto_increment'\n"
               + "        ELSE ''\n"
               + "    END AS extra\n"
               + "FROM  information_schema.columns\n"
               + "WHERE table_name = ? AND table_schema = current_schema()\n"
               + "ORDER BY  ordinal_position";
    }
}
