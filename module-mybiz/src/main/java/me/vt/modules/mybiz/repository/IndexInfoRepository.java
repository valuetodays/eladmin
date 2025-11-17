package me.vt.modules.mybiz.repository;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.mybiz.domain.IndexInfo;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@ApplicationScoped
public class IndexInfoRepository extends MyPanacheRepository<IndexInfo> {

    public List<IndexInfo> findTop10ByReleaseDateNullOrderByIdAsc(long lastId) {
        return find("releaseDate is null and csiCode is not null and id > ?1", Sort.ascending("id"), lastId)
                .page(Page.of(0, 10)).list();
    }

}
