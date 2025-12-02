package me.vt.modules.mybiz.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.HttpsDomainDto;
import me.vt.modules.mybiz.domain.HttpsDomain;
import me.vt.modules.mybiz.service.HttpsDomainServiceImpl;
import me.vt.modules.mybiz.service.dto.HttpsDomainQueryCriteria;
import me.vt.utils.PageResult;
import me.vt.utils.StringExUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author valuetodays
 * @since 2025-12-01 22:19
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "https域名管理")
@Path("/api/httpsDomain")
public class HttpsDomainController extends BaseController {

    @Inject
    HttpsDomainServiceImpl httpsDomainService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('httpsDomain:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(HttpsDomainQueryCriteria criteria) throws IOException {
        File file = httpsDomainService.download(httpsDomainService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询https域名管理")
    @PreAuthorize("@el.check('httpsDomain:list')")
    public PageResult<HttpsDomainDto> query(HttpsDomainQueryCriteria criteria) {
        return httpsDomainService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path(value = "/public/list")
    @Operation(summary = "查询https域名列表")
    @PreAuthorize("@el.check('httpsDomain:public_list')")
    public List<String> publicList() {
        List<HttpsDomainDto> list = httpsDomainService.queryAll(null);
        if (CollectionUtils.isEmpty(list)) {
            return List.of();
        }
        return list.stream()
            .map(HttpsDomainDto::getDomain)
            .map(e -> cn.vt.util.StringExUtils.makeSuffix(e, ":443"))
            .distinct()
            .toList();
    }

    @POST
    @Path("/add")
    @Log("新增https域名管理")
    @Operation(summary = "新增https域名管理")
    @PreAuthorize("@el.check('httpsDomain:add')")
    public Object create(HttpsDomain resources) {
        httpsDomainService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改https域名管理")
    @Operation(summary = "修改https域名管理")
    @PreAuthorize("@el.check('httpsDomain:edit')")
    public Object update(HttpsDomain resources) {
        httpsDomainService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除https域名管理")
    @Operation(summary = "删除https域名管理")
    @PreAuthorize("@el.check('httpsDomain:del')")
    public Object deleteHttpsDomain(Set<Long> ids) {
        httpsDomainService.delete(ids);
        return 1;
    }
}
