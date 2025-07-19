package me.zhengjie.modules.maint.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.domain.ServerDeploy;
import me.zhengjie.modules.maint.service.ServerDeployService;
import me.zhengjie.modules.maint.service.dto.ServerDeployDto;
import me.zhengjie.modules.maint.service.dto.ServerDeployQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Tag(name = "运维：服务器管理")
@RequiredArgsConstructor
@Path("/api/serverDeploy")
public class ServerDeployController extends BaseController {

    @Inject
    ServerDeployService serverDeployService;

    @Operation(summary = "导出服务器数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public Response exportServerDeploy(ServerDeployQueryCriteria criteria) throws IOException {
        File file = serverDeployService.download(serverDeployService.queryAll(criteria));
        return super.download(file);
    }

    @Operation(summary = "查询服务器")
    @GET
    @Path("")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public PageResult<ServerDeployDto> queryServerDeploy(ServerDeployQueryCriteria criteria) {
        return serverDeployService.queryAll(criteria, criteria.toPageRequest());
    }

    @Log("新增服务器")
    @Operation(summary = "新增服务器")
    @POST
    @Path("add")
    @PreAuthorize("@el.check('serverDeploy:add')")
    public Object createServerDeploy(@Valid ServerDeploy resources) {
        serverDeployService.create(resources);
        return 1;
    }

    @Log("修改服务器")
    @Operation(summary = "修改服务器")
    @POST
    @Path("edit")
    @PreAuthorize("@el.check('serverDeploy:edit')")
    public Object updateServerDeploy(@Valid ServerDeploy resources) {
        serverDeployService.update(resources);
        return 1;
    }

    @Log("删除服务器")
    @Operation(summary = "删除Server")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('serverDeploy:del')")
    public Object deleteServerDeploy(Set<Long> ids) {
        serverDeployService.delete(ids);
        return 1;
    }

    @Log("测试连接服务器")
    @Operation(summary = "测试连接服务器")
    @POST
    @Path("/testConnect")
    @PreAuthorize("@el.check('serverDeploy:add')")
    public Object testConnectServerDeploy(@Valid ServerDeploy resources) {
        return serverDeployService.testConnect(resources);
    }
}
