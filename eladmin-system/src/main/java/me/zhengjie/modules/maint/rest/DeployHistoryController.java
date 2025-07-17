package me.zhengjie.modules.maint.rest;

import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.service.DeployHistoryService;
import me.zhengjie.modules.maint.service.dto.DeployHistoryDto;
import me.zhengjie.modules.maint.service.dto.DeployHistoryQueryCriteria;
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
@RequiredArgsConstructor
@Tag(name = "运维：部署历史管理")
@Path("/api/deployHistory")
public class DeployHistoryController {

    @Inject
    DeployHistoryService deployhistoryService;

    @Operation(summary = "导出部署历史数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public void exportDeployHistory(HttpServletResponse response, DeployHistoryQueryCriteria criteria) throws IOException {
        deployhistoryService.download(deployhistoryService.queryAll(criteria), response);
    }

    @Operation(summary = "查询部署历史")
    @GET
    @Path
    @PreAuthorize("@el.check('deployHistory:list')")
    public ResponseEntity<PageResult<DeployHistoryDto>> queryDeployHistory(DeployHistoryQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(deployhistoryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("删除DeployHistory")
    @Operation(summary = "删除部署历史")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check('deployHistory:del')")
    public Object deleteDeployHistory(Set<String> ids) {
        deployhistoryService.delete(ids);
        return 1;
    }
}
