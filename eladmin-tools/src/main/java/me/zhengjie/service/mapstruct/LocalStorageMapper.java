package me.zhengjie.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.service.dto.LocalStorageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDto, LocalStorage> {

}
