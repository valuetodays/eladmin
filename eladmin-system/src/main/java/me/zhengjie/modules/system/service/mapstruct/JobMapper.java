package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.MapStructMapperConfig;
import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.service.dto.JobDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
 * @since 2019-03-29
*/
@Mapper(componentModel = "cdi", config = MapStructMapperConfig.class, uses = {DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapper extends BaseMapper<JobDto, Job> {
}
