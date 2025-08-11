package me.vt.modules.mybiz.service.mapstruct;

import me.vt.MapStructMapperConfig;
import me.vt.base.BaseMapper;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.service.dto.StockDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vt
 * @since 2025-08-11 19:56
 **/
@Mapper(componentModel = "jakarta",
    config = MapStructMapperConfig.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper extends BaseMapper<StockDto, Stock> {

}
