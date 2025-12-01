package me.vt.service1;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import me.vt.modules.mybiz.service.IndexInfoServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
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
        long lastId = 0L;
        while (true) {
            Pair<Boolean, Long> booleanLongPair = indexInfoServiceImpl.updateMissingFieldsFromApiForTop10(lastId);
            boolean done = booleanLongPair.getLeft();
            if (done) {
                break;
            }
            lastId = booleanLongPair.getRight();
            Thread.sleep(3000);
        }
    }
}
