package me.zhengjie;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingInheritanceStrategy;

@MapperConfig(
    mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG,
    componentModel = MappingConstants.ComponentModel.JAKARTA
)
public interface MapStructMapperConfig {
}
