package me.vt.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.statement.Query;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * .
 *
 * @author lei.liu
 * @since 2023-11-01
 */
@ApplicationScoped
@Slf4j
public class SqlServiceImpl {

    private final DataSource ds;
    @Getter
    private final Jdbi jdbi;

    public SqlServiceImpl(DataSource dataSource) {
        this.ds = dataSource;
        this.jdbi = Jdbi.create(ds);
    }

    @Transactional
    public AffectedRowsResp saveBySqls(List<String> sqls) {
        if (CollectionUtils.isEmpty(sqls)) {
            return AffectedRowsResp.empty();
        }
        int sum = sqls.stream().mapToInt(this::saveBySql).sum();
        return AffectedRowsResp.of(sum);
    }

    private static <T> ResultIterable<T> buildQueryResultIterable(Handle handle, String sql,
            Object[] params, Class<T> clazz) {
        try (Query query = handle.createQuery(sql)) {
            if (ArrayUtils.isNotEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    query.bind(i, params[i]); // 从0开始
                }
            }
            return query.mapToBean(clazz);
        }
    }

    private int saveBySql(String sql) {
        try (Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate();
        } catch (Exception e) {
            log.error("error when saveBySql", e);
        }
        return 0;
    }

    public <T> T queryForObject(String sql, Class<T> clazz, final Object... params) {
        return jdbi.withHandle(handle -> {
            ResultIterable<T> ri = buildQueryResultIterable(handle, sql, params, clazz);
            return ri.findOne().orElse(null);
        });
    }

    /**
     * queryForList.
     *
     * @param sql    sql字段需要as成和对象(T)属性一样。
     * @param clazz  clazz
     * @param params params
     * @param <T>    T
     * @return List(T)
     */
    public <T> List<T> queryForList(String sql, Class<T> clazz, final Object... params) {
        return jdbi.withHandle(handle -> {
            ResultIterable<T> ri = buildQueryResultIterable(handle, sql, params, clazz);
            return ri.list();
        });
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbi.withHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(sql);

            for (Object[] args : batchArgs) {
                for (int i = 0; i < args.length; i++) {
                    batch.bind(i, args[i]);
                }
                batch.add();
            }

            return batch.execute();
        });
    }

    public void execute(String sql) {
        jdbi.withHandle(handle -> handle.execute(sql));
    }
}
