package me.zhengjie.config;

import cn.hutool.core.util.StrUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@Data
@ApplicationScoped
public class RedissonConfiguration {
    @Inject
    @ConfigProperty(name = "spring.redis.host")
    private String redisHost;
    @Inject
    @ConfigProperty(name = "spring.redis.port")
    private int redisPort;
    @Inject
    @ConfigProperty(name = "spring.redis.database")
    private int redisDatabase;
    @Inject
    @ConfigProperty(name = "spring.redis.password")
    private String redisPassword;
    @Inject
    @ConfigProperty(name = "spring.redis.timeout")
    private int timeout;
    @Inject
    @ConfigProperty(name = "spring.redis.lettuce.pool.max-active")
    private int connectionPoolSize;
    @Inject
    @ConfigProperty(name = "spring.redis.lettuce.pool.min-idle")
    private int connectionMinimumIdleSize;

    @Produces
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setDatabase(redisDatabase)
                .setTimeout(timeout)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        if(StrUtil.isNotBlank(redisPassword)){
            config.useSingleServer().setPassword(redisPassword);
        }
        return Redisson.create(config);
    }
}


