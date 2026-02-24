package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.NationCodeDto;
import me.vt.modules.mybiz.domain.NationCode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NationCodeConverter extends BaseMapper<NationCodeDto, NationCode> {

}
