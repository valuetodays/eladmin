package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.MapStructMapperConfig;
import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.service.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@Mapper(componentModel = "jakarta",
    config = MapStructMapperConfig.class,
    uses = {MenuMapper.class, DeptMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleDto, Role> {

}
