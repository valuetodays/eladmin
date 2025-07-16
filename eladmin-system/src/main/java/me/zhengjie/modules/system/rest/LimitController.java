
package me.zhengjie.modules.system.rest;

import java.util.concurrent.atomic.AtomicInteger;

import me.zhengjie.annotation.Limit;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * @author /
 * 接口限流测试类
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/api/limit")
@Tag(name = "系统：限流测试管理")
public class LimitController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test，
     */
    @AnonymousGetMapping
    @Operation(summary = "测试")
    @Limit(key = "test", period = 60, count = 10, name = "testLimit", prefix = "limit")
    public int testLimit() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
