package me.vt.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ConfigMapping(prefix = "quarkus.redis")
@StaticInitSafe
@ApplicationScoped
public interface RedissonProperty {
    String hosts();

    int database();

    String password();

    Duration timeout();

    @WithDefault("4")
    int connectionPoolSize();

    @WithDefault("2")
    int connectionMinimumIdleSize();


}
