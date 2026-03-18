package me.vt.modules.mybiz.repository;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.db.SqlServiceImpl;
import me.vt.modules.mybiz.domain.StockInfoPersist;

import java.util.List;

@ApplicationScoped
public class StockInfoRepository extends MyPanacheRepository<StockInfoPersist> {

    @Inject
    SqlServiceImpl sqlService;

    public List<StockInfoPersist> findPopularList() {
        return find("popularFlag = :flag", Sort.ascending("id"), Parameters.with("flag", true)).list();
    }

    public void updateFenhongColumns() {
        // sql的作用是把 值【每份累计0.02元（5次）】中的0.02和5提取出来，分别赋值给相应字段
        String sql = """
            UPDATE f_stock_info t
            SET
                fenhong_total_amt_per_share = COALESCE(m[1]::numeric, 0),
                fenhong_times               = COALESCE(m2[1]::int, 0)
            FROM f_stock_info t2
            LEFT JOIN LATERAL regexp_match(t2.fenhong, '([0-9]+\\.?[0-9]*)') m ON TRUE
            LEFT JOIN LATERAL regexp_match(t2.fenhong, '（([0-9]+)次）') m2 ON TRUE
            WHERE t.id = t2.id AND t2.fenhong IS NOT NULL AND t2.fenhong <> '';
            """;
        sqlService.execute(sql);
    }

}
