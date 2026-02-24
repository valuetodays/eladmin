package me.vt.modules.mybiz.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import me.vt.modules.mybiz.api.dto.MetricDockerStatsDto;
import me.vt.modules.mybiz.domain.MetricDockerStats;
import me.vt.modules.mybiz.repository.MetricDockerStatsRepository;
import me.vt.modules.mybiz.service.dto.MetricDockerStatsQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.MetricDockerStatsConverter;
import me.vt.utils.FileUtil;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import me.vt.utils.ValidationUtil;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@ApplicationScoped
public class MetricDockerStatsServiceImpl {

    @Inject
    MetricDockerStatsRepository metricDockerStatsRepository;
    @Inject
    MetricDockerStatsConverter metricDockerStatsConverter;

    public PageResult<MetricDockerStatsDto> queryAll(MetricDockerStatsQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, MetricDockerStats.class);
        PanacheQuery<MetricDockerStats> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = metricDockerStatsRepository.findAll(sort);
        } else {
            panacheQuery = metricDockerStatsRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }
        PanacheQuery<MetricDockerStats> all = panacheQuery.page(pageable);
        List<MetricDockerStatsDto> list = metricDockerStatsConverter.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<MetricDockerStatsDto> queryAll(MetricDockerStatsQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public MetricDockerStatsDto findById(Long id) {
        MetricDockerStats metricDockerStats = metricDockerStatsRepository.findById(id);
        ValidationUtil.isNull(metricDockerStats.getId(), "MetricDockerStats", "id", id);
        return metricDockerStatsConverter.toDto(metricDockerStats);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(MetricDockerStats resources) {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        metricDockerStatsRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(MetricDockerStats resources) {
        MetricDockerStats metricDockerStats = metricDockerStatsRepository.findById(resources.getId());
        ValidationUtil.isNull(metricDockerStats.getId(), "MetricDockerStats", "id", resources.getId());
        metricDockerStats.copy(resources);
        metricDockerStatsRepository.save(metricDockerStats);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            metricDockerStatsRepository.deleteById(id);
        }
    }

    public File download(List<MetricDockerStatsDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MetricDockerStatsDto metricDockerStats : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("统计时间", metricDockerStats.getStatDatetime());
            map.put("名称", metricDockerStats.getName());
            map.put("a", metricDockerStats.getBlockIo());
            map.put("b", metricDockerStats.getNetIo());
            map.put("c", metricDockerStats.getCpuPerc());
            map.put("d", metricDockerStats.getContainerId());
            map.put("ee", metricDockerStats.getMemPerc());
            map.put("f", metricDockerStats.getMemUsage());
            map.put("创建时间", metricDockerStats.getCreateTime());
            map.put("修改日期", metricDockerStats.getUpdateTime());
            map.put("创建者id", metricDockerStats.getCreateUserId());
            map.put("更新者id", metricDockerStats.getUpdateUserId());
            map.put("ip", metricDockerStats.getIp());
            map.put("mem_usage", metricDockerStats.getMemUsage2());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }

    public List<MetricDockerStatsDto> chart() {
        String name = "api2-by-quarkus";
        List<MetricDockerStats> list = metricDockerStatsRepository.findAllByNameOrderByStatDatetimeDesc(name);
        list.sort(Comparator.comparing(MetricDockerStats::getStatDatetime));
        return metricDockerStatsConverter.toDto(list);
    }
}
