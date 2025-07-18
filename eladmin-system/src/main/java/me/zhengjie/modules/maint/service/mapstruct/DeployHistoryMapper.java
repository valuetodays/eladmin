package me.zhengjie.modules.maint.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.maint.domain.DeployHistory;
import me.zhengjie.modules.maint.service.dto.DeployHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Mapper(componentModel = "jakarta", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployHistoryMapper extends BaseMapper<DeployHistoryDto, DeployHistory> {

}
