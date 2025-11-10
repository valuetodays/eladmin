package me.vt.modules.maint.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.maint.domain.App;
import me.vt.modules.maint.service.dto.AppDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Mapper(componentModel = "jakarta", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppMapper extends BaseMapper<AppDto, App> {

}
