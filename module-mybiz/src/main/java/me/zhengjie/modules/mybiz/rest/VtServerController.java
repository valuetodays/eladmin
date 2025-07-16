
package me.zhengjie.modules.mybiz.rest;

import java.io.IOException;
import java.time.LocalDateTime;

import io.quarkus.panache.common.Page;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.service.VtServerService;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import me.zhengjie.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author vt
 * @website https://eladmin.vip
 * @date 2025-07-11
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "VtServerController")
@Path("/api/vtServer")
public class VtServerController {

    @Inject
    VtServerService vtServerService;

    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('vtServer:list')")
    public void exportVtServer(HttpServletResponse response, VtServerQueryCriteria criteria) throws IOException {
        vtServerService.download(vtServerService.queryAll(criteria), response);
    }

    @GET
    @Path
    @Operation(summary = "查询VtServerController")
    @PreAuthorize("@el.check('vtServer:list')")
    public ResponseEntity<PageResult<VtServerDto>> queryVtServer(VtServerQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(vtServerService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @POST
    @Path("")
    @Log("新增VtServerController")
    @Operation(summary = "新增VtServerController")
    @PreAuthorize("@el.check('vtServer:add')")
    public Object createVtServer(@Valid VtServer resources) {
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        String username = currentUser.getUsername();
        resources.setCreateBy(username);
        resources.setUpdateBy(username);
        resources.setCreateTime(LocalDateTime.now());
        resources.setUpdateTime(LocalDateTime.now());
        vtServerService.create(resources);
        return 1;
    }

    @PUT
    @Path("")
    @Log("修改VtServerController")
    @Operation(summary = "修改VtServerController")
    @PreAuthorize("@el.check('vtServer:edit')")
    public Object updateVtServer(@Valid VtServer resources) {
        vtServerService.update(resources);
        return 1;
    }

    @DELETE
    @Path("")
    @Log("删除VtServerController")
    @Operation(summary = "删除VtServerController")
    @PreAuthorize("@el.check('vtServer:del')")
    public Object deleteVtServer(@ApiParam(value = "传ID数组[]") Long[] ids) {
        vtServerService.deleteAll(ids);
        return 1;
    }
}