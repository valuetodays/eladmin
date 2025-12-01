package me.vt.modules.system.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.system.domain.Dept;
import me.vt.modules.system.service.dto.DeptSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptSmallMapper extends BaseMapper<DeptSmallDto, Dept> {

}
