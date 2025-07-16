
package me.zhengjie.modules.system.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.service.MonitorService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Zheng Jie
 * @date 2020-05-02
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "系统-服务监控管理")
@Path("/api/monitor")
public class MonitorController {

    @Inject
    MonitorService serverService;

    @GET
    @Path("")
    @Operation(summary = "查询服务监控")
    @PreAuthorize("@el.check('monitor:list')")
    public Object queryMonitor() {
        return serverService.getServers();
    }
}
