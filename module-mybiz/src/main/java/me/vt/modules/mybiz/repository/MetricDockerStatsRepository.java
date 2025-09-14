package me.vt.modules.mybiz.repository;

import io.quarkus.panache.common.Page;
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

    public List<MetricDockerStats> findAllByNameOrderByStatDatetimeDesc(String name) {
        return find(
            "select m.statDatetime, m.memUsage2 from MetricDockerStats m where m.name = ?1",
            Sort.descending("statDatetime"),
            name
        ).project(MetricDockerStats.class).page(Page.of(0, 1000)).list();
    }
}
