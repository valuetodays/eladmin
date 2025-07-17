package me.zhengjie.modules.maint.rest;

import io.quarkus.panache.common.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.domain.ServerDeploy;
import me.zhengjie.modules.maint.service.ServerDeployService;
import me.zhengjie.modules.maint.service.dto.ServerDeployDto;
import me.zhengjie.modules.maint.service.dto.ServerDeployQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
public class ServerDeployController {

    @Inject
    ServerDeployService serverDeployService;

    @Operation(summary = "导出服务器数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public void exportServerDeploy(HttpServletResponse response, ServerDeployQueryCriteria criteria) throws IOException {
        serverDeployService.download(serverDeployService.queryAll(criteria), response);
    }

    @Operation(summary = "查询服务器")
    @GET
    @Path
    @PreAuthorize("@el.check('serverDeploy:list')")
    public ResponseEntity<PageResult<ServerDeployDto>> queryServerDeploy(ServerDeployQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(serverDeployService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增服务器")
    @Operation(summary = "新增服务器")
    @POST
    @Path("")
    @PreAuthorize("@el.check('serverDeploy:add')")
    public Object createServerDeploy(@Valid ServerDeploy resources) {
        serverDeployService.create(resources);
        return 1;
    }

    @Log("修改服务器")
    @Operation(summary = "修改服务器")
    @PUT
    @Path("")
    @PreAuthorize("@el.check('serverDeploy:edit')")
    public Object updateServerDeploy(@Valid ServerDeploy resources) {
        serverDeployService.update(resources);
        return 1;
    }

    @Log("删除服务器")
    @Operation(summary = "删除Server")
    @DELETE
    @Path("")
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
        return new ResponseEntity<>(serverDeployService.testConnect(resources),HttpStatus.CREATED);
    }
}
