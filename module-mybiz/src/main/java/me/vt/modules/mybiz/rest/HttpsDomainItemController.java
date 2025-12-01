package me.vt.modules.mybiz.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.HttpsDomainItemDto;
import me.vt.modules.mybiz.domain.HttpsDomainItem;
import me.vt.modules.mybiz.service.HttpsDomainItemServiceImpl;
import me.vt.modules.mybiz.service.dto.HttpsDomainItemQueryCriteria;
import me.vt.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author valuetodays
 * @since 2025-12-01 22:19
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "https域名管理")
@Path("/api/httpsDomainItem")
public class HttpsDomainItemController extends BaseController {

    @Inject
    HttpsDomainItemServiceImpl httpsDomainItemService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('httpsDomainItem:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(HttpsDomainItemQueryCriteria criteria) throws IOException {
        File file = httpsDomainItemService.download(httpsDomainItemService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询https域名管理")
    @PreAuthorize("@el.check('httpsDomainItem:list')")
    public PageResult<HttpsDomainItemDto> query(HttpsDomainItemQueryCriteria criteria) {
        return httpsDomainItemService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path(value = "/public/list")
    @Operation(summary = "查询https域名列表")
    @PreAuthorize("@el.check('httpsDomainItem:public_list')")
    public List<String> publicList() {
//        return httpsDomainItemService.queryAll(null);
        return List.of("eblog.sisiruyi.fun:443"
            , "www.sisiruyi.fun:443"
            , "doc.sisiruyi.fun:443"
            , "chat.sisiruyi.fun:443");
    }

    @POST
    @Path("/add")
    @Log("新增https域名管理")
    @Operation(summary = "新增https域名管理")
    @PreAuthorize("@el.check('httpsDomainItem:add')")
    public Object create(HttpsDomainItem resources) {
        httpsDomainItemService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改https域名管理")
    @Operation(summary = "修改https域名管理")
    @PreAuthorize("@el.check('httpsDomainItem:edit')")
    public Object update(HttpsDomainItem resources) {
        httpsDomainItemService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除https域名管理")
    @Operation(summary = "删除https域名管理")
    @PreAuthorize("@el.check('httpsDomainItem:del')")
    public Object deleteHttpsDomainItem(Set<Long> ids) {
        httpsDomainItemService.delete(ids);
        return 1;
    }
}
