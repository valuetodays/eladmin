package me.zhengjie;

import cn.valuetodays.quarkus.commons.base.auth.TokenInCacheAuthUserParser;
import cn.vt.auth.AuthUserParser;
import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import me.zhengjie.config.RedissonProperty;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * this class to expose bean that can not be injected automatically.
 *
 * @author lei.liu
 * @since 2025-06-04
 */
@ApplicationScoped
public class BeansProducer {
    @Inject
    RedisDataSource stringRedisTemplate;

    @Produces
    public AuthUserParser tokenInCacheAuthUserParser() {
        TokenInCacheAuthUserParser t = new TokenInCacheAuthUserParser();
        t.setStringRedisTemplate(stringRedisTemplate);
        return t;
    }

    @Produces
    public RedissonClient redissonClient(RedissonProperty p) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(p.hosts())
                .setDatabase(p.database())
                .setTimeout((int) p.timeout().toMillis())
                .setConnectionPoolSize(p.connectionPoolSize())
                .setConnectionMinimumIdleSize(p.connectionMinimumIdleSize());
        if (StringUtils.isNotBlank(p.password())) {
            config.useSingleServer().setPassword(p.password());
        }
        return Redisson.create(config);
    }
}
