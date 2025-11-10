package me.vt.modules.system.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.system.domain.Role;
import me.vt.modules.system.service.dto.RoleSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @since 2019-5-23
 */
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapper extends BaseMapper<RoleSmallDto, Role> {

}
