package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.MapStructMapperConfig;
import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.HttpsDomainItemDto;
import me.vt.modules.mybiz.domain.HttpsDomainItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@Mapper(componentModel = "jakarta",
config = MapStructMapperConfig.class,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HttpsDomainItemMapper extends BaseMapper<HttpsDomainItemDto, HttpsDomainItem> {

}
