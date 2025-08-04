package me.zhengjie;

import cn.vt.util.StringExUtils;
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
        String avatarPath = StringExUtils.removePrefixIfNecessary(fileProperties.getAvatar(), "/");
        String path = StringExUtils.removePrefixIfNecessary(fileProperties.getPath(), "/");
        router.route("/avatar/*").handler(StaticHandler.create(avatarPath).setCachingEnabled(false));
        router.route("/file/*").handler(StaticHandler.create(path).setCachingEnabled(false));
    }
}
