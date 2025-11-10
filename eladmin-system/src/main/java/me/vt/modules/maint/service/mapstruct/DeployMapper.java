package me.vt.modules.maint.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.maint.domain.Deploy;
import me.vt.modules.maint.service.dto.DeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Mapper(componentModel = "jakarta", uses = {AppMapper.class, ServerDeployMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployMapper extends BaseMapper<DeployDto, Deploy> {

}
