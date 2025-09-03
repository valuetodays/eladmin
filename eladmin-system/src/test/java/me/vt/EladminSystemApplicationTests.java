package me.vt;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.VtServerDto;
import me.vt.modules.mybiz.service.VtServerServiceImpl;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Slf4j
public class EladminSystemApplicationTests {

    @Inject
    VtServerServiceImpl vtServerService;

    @Test
    public void contextLoads() {
        VtServerDto byId = vtServerService.findById(1L);
        log.info("byId={}", byId);
    }

}

