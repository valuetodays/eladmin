package me.vt.modules.mybiz.service.mapstruct;

import me.vt.base.BaseMapper;
import me.vt.modules.mybiz.domain.VtServer;
import me.vt.modules.mybiz.service.dto.VtServerDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vt

 * @since 2025-07-11
 **/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VtServerMapper extends BaseMapper<VtServerDto, VtServer> {

}
