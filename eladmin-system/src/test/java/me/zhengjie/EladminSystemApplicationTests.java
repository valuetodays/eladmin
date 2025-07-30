package me.zhengjie;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.mybiz.service.VtServerService;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Slf4j
public class EladminSystemApplicationTests {

    @Inject
    VtServerService vtServerService;

    @Test
    public void contextLoads() {
        VtServerDto byId = vtServerService.findById(1L);
        log.info("byId={}", byId);
    }

}

