package me.zhengjie.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.SysLog;
import me.zhengjie.service.dto.SysLogErrorDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @since 2019-5-22
 */
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapper extends BaseMapper<SysLogErrorDto, SysLog> {

}
