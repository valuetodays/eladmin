package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.MapStructMapperConfig;
import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.StockDailyIndicatorDto;
import me.vt.modules.mybiz.domain.StockDailyIndicator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author valuetodays
 * @since 2025-11-18 20:01
 **/
@Mapper(componentModel = "jakarta",
        config = MapStructMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockDailyIndicatorConverter extends BaseMapper<StockDailyIndicatorDto, StockDailyIndicator> {

    @Override
    @Mapping(target = "code", source = "id.code")
    @Mapping(target = "statDate", source = "id.statDate")
    StockDailyIndicatorDto toDto(StockDailyIndicator entity);
}
