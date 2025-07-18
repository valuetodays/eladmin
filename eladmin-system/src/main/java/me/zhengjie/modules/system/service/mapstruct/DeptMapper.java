package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.MapStructMapperConfig;
import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.service.dto.DeptDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @since 2019-03-25
 */
@Mapper(componentModel = "cdi", config = MapStructMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper extends BaseMapper<DeptDto, Dept> {
}
