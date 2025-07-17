package me.zhengjie.config.properties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Getter
public class FileProperties {

    @Inject
    @ConfigProperty(name = "file.max-size")
    Long maxSize;

    @Inject
    @ConfigProperty(name = "file.avatar-max-size")
    Long avatarMaxSize;

    @Inject
    ElPathConfig mac;

    @Inject
    ElPathConfig linux;

    @Inject
    ElPathConfig windows;

    public ElPathConfig getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return windows;
        } else if (os.toLowerCase().startsWith("mac")) {
            return mac;
        }
        return linux;
    }

    @ApplicationScoped
    public static class ElPathConfig {

        @Inject
        @ConfigProperty(name = "file.mac.path", defaultValue = "")
        String path;

        @Inject
        @ConfigProperty(name = "file.mac.avatar", defaultValue = "")
        String avatar;

        public String getPath() {
            return path;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
