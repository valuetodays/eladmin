package me.vt.modules.system.service.mapstruct;

import me.vt.MapStructMapperConfig;
import me.vt.base.BaseMapper;
import me.vt.modules.system.domain.User;
import me.vt.modules.system.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "jakarta",
    config = MapStructMapperConfig.class,
    uses = {RoleMapper.class, DeptMapper.class, JobMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {
}
