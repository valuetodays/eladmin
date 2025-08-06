package me.vt.service1;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.vt.domain.vo.TableInfo;
import me.vt.service.GeneratorService;
import me.vt.utils.PageResult;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link GeneratorService}.
 *
 * @author lei.liu
 * @since 2025-08-04
 */
@QuarkusTest
@Slf4j
public class GeneratorServiceTest {

    @Inject
    GeneratorService generatorService;

    @Test
    public void getTables() {
        PageResult<TableInfo> ba = generatorService.getTables("ba", new int[]{0, 10});
        log.info("ba={}", ba);
    }
}
