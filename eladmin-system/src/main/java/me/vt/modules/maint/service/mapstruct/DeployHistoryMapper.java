package me.vt.modules.maint.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.maint.domain.DeployHistory;
import me.vt.modules.maint.service.dto.DeployHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Mapper(componentModel = "jakarta", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployHistoryMapper extends BaseMapper<DeployHistoryDto, DeployHistory> {

}
