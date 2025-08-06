package me.vt.service;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-14
 */
public class PgSqlTableInfoGather implements DatabaseTableInfoGather {
    @Override
    public String getSqlForTables() {
        return """
                SELECT
                    c.relname AS table_name,
                    obj_description(c.oid) AS table_comment
                FROM pg_class c
                JOIN  pg_namespace n ON n.oid = c.relnamespace
                WHERE  c.relkind = 'r'  AND n.nspname = 'public'
                ORDER BY  c.oid DESC
                """;
    }

    @Override
    public String getSqlForTablesForQuery() {
        return """
                SELECT
                    c.relname AS table_name,
                    null as create_time, null as engine, null as table_collation,     obj_description(c.oid) AS table_comment
                FROM  pg_class c
                JOIN  pg_namespace n ON n.oid = c.relnamespace
                WHERE
                    c.relkind = 'r'
                    AND n.nspname = 'public'     AND c.relname like :table ORDER BY  c.oid DESC
                """;
    }

    @Override
    public String getSqlForColumns(String name) {
        return """
            SELECT
                cols.column_name,
                cols.is_nullable,
                cols.data_type,
                pgd.description AS column_comment,
                CASE
                    WHEN tc.constraint_type = 'PRIMARY KEY' THEN 'PRI'
                    ELSE ''
                END AS column_key,
                CASE
                    WHEN is_identity = 'YES' THEN 'auto_increment'
                    ELSE ''
                END AS extra
            FROM information_schema.columns cols
            LEFT JOIN pg_catalog.pg_statio_all_tables as st
                ON st.relname = cols.table_name
            LEFT JOIN pg_catalog.pg_description pgd
                ON pgd.objoid = st.relid
                AND pgd.objsubid = cols.ordinal_position
            LEFT JOIN information_schema.key_column_usage kcu
                ON cols.table_name = kcu.table_name
                AND cols.column_name = kcu.column_name
                AND cols.table_schema = kcu.table_schema
            LEFT JOIN information_schema.table_constraints tc
                ON tc.constraint_name = kcu.constraint_name
                AND tc.table_schema = kcu.table_schema
                AND tc.constraint_type = 'PRIMARY KEY'
            WHERE cols.table_name = ?
            ORDER BY cols.ordinal_position;
            """;
    }
}
