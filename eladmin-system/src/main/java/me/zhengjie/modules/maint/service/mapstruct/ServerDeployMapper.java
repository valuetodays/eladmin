
package me.zhengjie.modules.maint.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.maint.domain.ServerDeploy;
import me.zhengjie.modules.maint.service.dto.ServerDeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapper extends BaseMapper<ServerDeployDto, ServerDeploy> {

}
