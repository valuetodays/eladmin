package me.zhengjie.modules.mybiz.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.mybiz.domain.NationCode;
import me.zhengjie.modules.mybiz.service.dto.NationCodeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NationCodeMapper extends BaseMapper<NationCodeDto, NationCode> {

}