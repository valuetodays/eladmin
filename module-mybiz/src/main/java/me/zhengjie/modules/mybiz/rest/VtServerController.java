package me.zhengjie.modules.mybiz.rest;

import cn.vt.auth.AuthUser;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.service.VtServerService;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import me.zhengjie.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author vt

 * @since 2025-07-11
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "VtServerController")
@Path("/api/vtServer")
public class VtServerController extends BaseController {

    @Inject
    VtServerService vtServerService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('vtServer:list')")
    public Response exportVtServer(VtServerQueryCriteria criteria) throws IOException {
        File file = vtServerService.download(vtServerService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path("query")
    @Operation(summary = "查询VtServerController")
    @PreAuthorize("@el.check('vtServer:list')")
    public PageResult<VtServerDto> queryVtServer(VtServerQueryCriteria criteria) {
        return vtServerService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("add")
    @Log("新增VtServerController")
    @Operation(summary = "新增VtServerController")
    @PreAuthorize("@el.check('vtServer:add')")
    public Object createVtServer(@Valid VtServer resources) {
        AuthUser currentUser = getCurrentAccount();
        String username = currentUser.getEmail();
        resources.setCreateBy(username);
        resources.setUpdateBy(username);
        resources.setCreateTime(LocalDateTime.now());
        resources.setUpdateTime(LocalDateTime.now());
        vtServerService.create(resources);
        return 1;
    }

    @POST
    @Path("edit")
    @Log("修改VtServerController")
    @Operation(summary = "修改VtServerController")
    @PreAuthorize("@el.check('vtServer:edit')")
    public Object updateVtServer(@Valid VtServer resources) {
        vtServerService.update(resources);
        return 1;
    }

    @POST
    @Path("/delete")
    @Log("删除VtServerController")
    @Operation(summary = "删除VtServerController")
    @PreAuthorize("@el.check('vtServer:del')")
    public Object deleteVtServer(Long[] ids) {
        vtServerService.deleteAll(ids);
        return 1;
    }
}
