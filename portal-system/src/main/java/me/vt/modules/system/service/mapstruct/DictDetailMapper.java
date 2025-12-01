package me.vt.modules.system.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.modules.system.domain.DictDetail;
import me.vt.modules.system.service.dto.DictDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@Mapper(componentModel = "jakarta", uses = {DictSmallMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailMapper extends BaseMapper<DictDetailDto, DictDetail> {

}
