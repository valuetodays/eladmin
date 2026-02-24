package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.MapStructMapperConfig;
import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.StockDailyQuoteDto;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author valuetodays
 * @since 2025-11-17 19:01
 **/
@Mapper(componentModel = "jakarta",
        config = MapStructMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockDailyQuoteConverter extends BaseMapper<StockDailyQuoteDto, StockDailyQuote> {

}
