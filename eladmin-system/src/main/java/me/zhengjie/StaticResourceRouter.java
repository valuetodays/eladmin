package me.zhengjie;

import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import me.zhengjie.config.properties.FileProperties;

@ApplicationScoped
public class StaticResourceRouter {
    @Inject
    FileProperties fileProperties;

    @Inject
    Router router;

    public void onStart(@Observes StartupEvent ev) {
        router.route("/avatar/*")
            .handler(StaticHandler.create(fileProperties.getAvatar()).setCachingEnabled(false));
        router.route("/file/*")
            .handler(StaticHandler.create(fileProperties.getPath()).setCachingEnabled(false));
    }
}
