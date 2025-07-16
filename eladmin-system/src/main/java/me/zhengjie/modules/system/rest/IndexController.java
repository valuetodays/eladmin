package me.zhengjie.modules.system.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-16
 */
@Path("/")
@Tag(name = "Index服务")
public class IndexController {
    @GET
    @Path("")
    public String index() {
        return "Backend service started successfully";
    }
}
