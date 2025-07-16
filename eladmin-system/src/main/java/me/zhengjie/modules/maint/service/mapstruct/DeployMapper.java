
package me.zhengjie.modules.maint.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.maint.domain.Deploy;
import me.zhengjie.modules.maint.service.dto.DeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Mapper(componentModel = "spring",uses = {AppMapper.class, ServerDeployMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployMapper extends BaseMapper<DeployDto, Deploy> {

}
