package me.vt.config.properties;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Getter
public class FileProperties {

    @ConfigProperty(name = "file.max-size")
    Long maxSize;

    @ConfigProperty(name = "file.avatar-max-size")
    Long avatarMaxSize;

    @ConfigProperty(name = "file.path")
    String path;
    @ConfigProperty(name = "file.avatar")
    String avatar;

}
