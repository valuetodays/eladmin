package me.vt.modules.mybiz.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.mybiz.api.dto.VtServerDto;
import me.vt.modules.mybiz.domain.VtServer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vt

 * @since 2025-07-11
 **/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VtServerMapper extends BaseMapper<VtServerDto, VtServer> {

}
