package me.vt.modules.system.service.mapstruct;

import me.vt.MapStructMapperConfig;
import me.vt.base.BaseMapper;
import me.vt.modules.system.domain.Menu;
import me.vt.modules.system.service.dto.MenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @since 2018-12-17
 */
@Mapper(componentModel = "jakarta", config = MapStructMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<MenuDto, Menu> {
}
