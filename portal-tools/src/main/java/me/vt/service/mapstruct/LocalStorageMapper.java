package me.vt.service.mapstruct;

import me.vt.common.base.BaseMapper;
import me.vt.domain.LocalStorage;
import me.vt.service.dto.LocalStorageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDto, LocalStorage> {

}
