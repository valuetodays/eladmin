package me.vt.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.domain.SysLog;
import me.vt.service.dto.SysLogErrorDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @since 2019-5-22
 */
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapper extends BaseMapper<SysLogErrorDto, SysLog> {

}
