package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.MapStructMapperConfig;
import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.MetricDockerStatsDto;
import me.vt.modules.mybiz.domain.MetricDockerStats;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@Mapper(componentModel = "jakarta",
        config = MapStructMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetricDockerStatsMapper extends BaseMapper<MetricDockerStatsDto, MetricDockerStats> {

}
