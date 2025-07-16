
package me.zhengjie.modules.maint.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.maint.domain.Database;
import me.zhengjie.modules.maint.service.dto.DatabaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseMapper extends BaseMapper<DatabaseDto, Database> {

}
