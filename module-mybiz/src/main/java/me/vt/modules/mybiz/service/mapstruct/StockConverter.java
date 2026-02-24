package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.MapStructMapperConfig;
import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.StockDto;
import me.vt.modules.mybiz.domain.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vt
 * @since 2025-08-11 19:56
 **/
@Mapper(componentModel = "jakarta",
        config = MapStructMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockConverter extends BaseMapper<StockDto, Stock> {

}
