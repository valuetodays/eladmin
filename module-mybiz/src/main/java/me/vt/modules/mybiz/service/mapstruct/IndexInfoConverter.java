package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.MapStructMapperConfig;
import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.IndexInfoDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@Mapper(componentModel = "jakarta",
        config = MapStructMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexInfoConverter extends BaseMapper<IndexInfoDto, IndexInfo> {

}
