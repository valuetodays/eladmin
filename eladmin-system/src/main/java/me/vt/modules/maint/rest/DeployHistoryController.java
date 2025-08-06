package me.vt.modules.maint.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.vt.BaseController;
import me.vt.annotation.Log;
import me.vt.modules.maint.service.DeployHistoryService;
import me.vt.modules.maint.service.dto.DeployHistoryDto;
import me.vt.modules.maint.service.dto.DeployHistoryQueryCriteria;
import me.vt.utils.PageResult;
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
@RequiredArgsConstructor
@Tag(name = "运维：部署历史管理")
@Path("/api/deployHistory")
public class DeployHistoryController extends BaseController {

    @Inject
    DeployHistoryService deployhistoryService;

    @Operation(summary = "导出部署历史数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public Response exportDeployHistory(DeployHistoryQueryCriteria criteria) throws IOException {
        File file = deployhistoryService.download(deployhistoryService.queryAll(criteria));
        return super.download(file);
    }

    @Operation(summary = "查询部署历史")
    @POST
    @Path("query")
    @PreAuthorize("@el.check('deployHistory:list')")
    public PageResult<DeployHistoryDto> queryDeployHistory(DeployHistoryQueryCriteria criteria) {
        return deployhistoryService.queryAll(criteria, criteria.toPageRequest());
    }

    @Log("删除DeployHistory")
    @Operation(summary = "删除部署历史")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('deployHistory:del')")
    public Object deleteDeployHistory(Set<Long> ids) {
        deployhistoryService.delete(ids);
        return 1;
    }
}
