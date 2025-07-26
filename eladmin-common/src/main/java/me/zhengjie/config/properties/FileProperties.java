package me.zhengjie.config.properties;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
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

    public ElPathConfig getPath() {
        ElPathConfig elPathConfig = new ElPathConfig();
        elPathConfig.setAvatar(avatar);
        elPathConfig.setPath(path);
        return elPathConfig;
    }

    @Data
    public static class ElPathConfig {
        private String path;
        private String avatar;
    }
}
