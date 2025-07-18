package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.service.dto.DictDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper extends BaseMapper<DictDto, Dict> {

}
