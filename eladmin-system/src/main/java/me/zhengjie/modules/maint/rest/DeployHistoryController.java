package me.zhengjie.modules.maint.rest;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.service.DeployHistoryService;
import me.zhengjie.modules.maint.service.dto.DeployHistoryDto;
import me.zhengjie.modules.maint.service.dto.DeployHistoryQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "运维：部署历史管理")
@Path("/api/deployHistory")
public class DeployHistoryController extends BaseController {

    @Inject
    DeployHistoryService deployhistoryService;

    @Operation(summary = "导出部署历史数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public Response exportDeployHistory(DeployHistoryQueryCriteria criteria) throws IOException {
        File file = deployhistoryService.download(deployhistoryService.queryAll(criteria));
        return super.download(file);
    }

    @Operation(summary = "查询部署历史")
    @GET
    @Path("")
    @PreAuthorize("@el.check('deployHistory:list')")
    public PageResult<DeployHistoryDto> queryDeployHistory(DeployHistoryQueryCriteria criteria) {
        return deployhistoryService.queryAll(criteria, criteria.toPageRequest());
    }

    @Log("删除DeployHistory")
    @Operation(summary = "删除部署历史")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check('deployHistory:del')")
    public Object deleteDeployHistory(Set<Long> ids) {
        deployhistoryService.delete(ids);
        return 1;
    }
}
