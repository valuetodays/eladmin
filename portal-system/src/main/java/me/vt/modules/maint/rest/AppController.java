package me.vt.modules.maint.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.maint.domain.App;
import me.vt.modules.maint.service.client.AppService;
import me.vt.modules.maint.service.dto.AppDto;
import me.vt.modules.maint.service.dto.AppQueryCriteria;
import me.vt.utils.PageResult;
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
@Tag(name = "运维：应用管理")
@Path("/api/app")
public class AppController extends BaseController {

    @Inject
    AppService appService;

    @Operation(summary = "导出应用数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('app:list')")
    public void exportApp(AppQueryCriteria criteria) throws IOException {
        appService.download(appService.queryAll(criteria));
    }

    @Operation(summary = "查询应用")
    @POST
    @Path("query")
    @PreAuthorize("@el.check('app:list')")
    public PageResult<AppDto> queryApp(AppQueryCriteria criteria) {
        return appService.queryAll(criteria, criteria.toPageRequest());
    }

    @Log("新增应用")
    @Operation(summary = "新增应用")
    @POST
    @Path("add")
    @PreAuthorize("@el.check('app:add')")
    public Object createApp(@Valid App resources) {
        appService.create(resources);
        return 1;
    }

    @Log("修改应用")
    @Operation(summary = "修改应用")
    @POST
    @Path("edit")
    @PreAuthorize("@el.check('app:edit')")
    public Object updateApp(@Valid App resources) {
        appService.update(resources);
        return 1;
    }

    @Log("删除应用")
    @Operation(summary = "删除应用")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('app:del')")
    public Object deleteApp(Set<Long> ids) {
        appService.delete(ids);
        return 1;
    }
}
