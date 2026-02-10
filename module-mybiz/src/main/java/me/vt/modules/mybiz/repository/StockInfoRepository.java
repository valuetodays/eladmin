package me.vt.modules.mybiz.repository;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.mybiz.domain.StockInfoPersist;

@ApplicationScoped
public class StockInfoRepository extends MyPanacheRepository<StockInfoPersist> {

    public List<StockInfoPersist> findPopularList() {
        return find("popularFlag = :flag", Sort.ascending("id"), Parameters.with("flag", true)).list();
    }

}
