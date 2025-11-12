package me.vt.service1;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import me.vt.modules.mybiz.service.IndexInfoServiceImpl;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-12
 */
@QuarkusTest
public class IndexInfoServiceImplTest {
    @Inject
    IndexInfoServiceImpl indexInfoServiceImpl;

    @Test
    public void updateMissingFieldsFromApi() throws InterruptedException {
        while (true) {
            boolean done = indexInfoServiceImpl.updateMissingFieldsFromApiForTop10();
            if (done) {
                break;
            }
            Thread.sleep(1500);
        }
    }
}
