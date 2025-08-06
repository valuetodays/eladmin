package me.vt.modules.mybiz.service.mapstruct;

import me.vt.base.BaseMapper;
import me.vt.modules.mybiz.domain.NationCode;
import me.vt.modules.mybiz.service.dto.NationCodeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NationCodeMapper extends BaseMapper<NationCodeDto, NationCode> {

}
