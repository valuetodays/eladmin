
package me.zhengjie.modules.maint.rest;

import java.io.IOException;
import java.util.Set;

import io.quarkus.panache.common.Page;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.domain.App;
import me.zhengjie.modules.maint.service.AppService;
import me.zhengjie.modules.maint.service.dto.AppDto;
import me.zhengjie.modules.maint.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "运维：应用管理")
@Path("/api/app")
public class AppController {

    @Inject
    AppService appService;

    @Operation(summary = "导出应用数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('app:list')")
    public void exportApp(HttpServletResponse response, AppQueryCriteria criteria) throws IOException {
        appService.download(appService.queryAll(criteria), response);
    }

    @Operation(summary = "查询应用")
    @GET
    @Path("")
    @PreAuthorize("@el.check('app:list')")
    public ResponseEntity<PageResult<AppDto>> queryApp(AppQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(appService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增应用")
    @Operation(summary = "新增应用")
    @POST
    @Path("")
    @PreAuthorize("@el.check('app:add')")
    public Object createApp(@Valid App resources) {
        appService.create(resources);
        return 1;
    }

    @Log("修改应用")
    @Operation(summary = "修改应用")
    @PUT
    @Path("")
    @PreAuthorize("@el.check('app:edit')")
    public Object updateApp(@Valid App resources) {
        appService.update(resources);
        return 1;
    }

    @Log("删除应用")
    @Operation(summary = "删除应用")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check('app:del')")
    public Object deleteApp(Set<Long> ids) {
        appService.delete(ids);
        return 1;
    }
}
