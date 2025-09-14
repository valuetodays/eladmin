package me.vt.modules.mybiz.repository;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import me.vt.MyPanacheRepository;
import me.vt.modules.mybiz.domain.MetricDockerStats;

import java.util.List;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@ApplicationScoped
public class MetricDockerStatsRepository extends MyPanacheRepository<MetricDockerStats> {

    public List<MetricDockerStats> findAllByNameOrderByStatDatetimeAsc(String name) {
        return find("name = ?1", Sort.ascending("statDatetime"), name).list();
    }
}
