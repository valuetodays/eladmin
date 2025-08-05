package ${package}.service.mapstruct;

import me.zhengjie.MapStructMapperConfig;
import me.zhengjie.base.BaseMapper;
import ${package}.domain.${className};
import ${package}.service.dto.${className}Dto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author ${author}
* @since ${.now?string("yyyy-MM-dd HH:mm")}
**/
@Mapper(componentModel = "jakarta",
config = MapStructMapperConfig.class,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}Mapper extends BaseMapper<${className}Dto, ${className}> {

}
