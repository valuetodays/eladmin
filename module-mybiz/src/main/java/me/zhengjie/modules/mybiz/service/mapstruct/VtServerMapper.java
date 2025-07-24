package me.zhengjie.modules.mybiz.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vt

 * @since 2025-07-11
 **/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VtServerMapper extends BaseMapper<VtServerDto, VtServer> {

}
