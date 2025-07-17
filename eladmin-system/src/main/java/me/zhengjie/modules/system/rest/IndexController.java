package me.zhengjie.modules.system.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import me.zhengjie.BaseController;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-16
 */
@Path("/")
@Tag(name = "Index服务")
public class IndexController extends BaseController {
    @GET
    @Path("")
    public String index() {
        return "Backend service started successfully";
    }
}
