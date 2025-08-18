package ${package}.rest;

import me.vt.annotation.Log;
import me.vt.BaseController;
import ${package}.domain.${className};
import ${package}.service.${className}ServiceImpl;
import ${package}.service.dto.${className}QueryCriteria;
import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.vt.utils.PageResult;
import ${package}.service.dto.${className}Dto;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

/**
 * @author ${author}
 * @since ${.now?string("yyyy-MM-dd HH:mm")}
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "${apiAlias}")
@Path("/api/${changeClassName}")
public class ${className}Controller extends BaseController {

    @Inject
    ${className}ServiceImpl ${changeClassName}Service;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('${changeClassName}:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(${className}QueryCriteria criteria) throws IOException {
        File file = ${changeClassName}Service.download(${changeClassName}Service.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public PageResult<${className}Dto> query(${className}QueryCriteria criteria) {
        return ${changeClassName}Service.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("/add")
    @Log("新增${apiAlias}")
    @Operation(summary = "新增${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:add')")
    public Object create(${className} resources) {
        ${changeClassName}Service.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改${apiAlias}")
    @Operation(summary = "修改${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:edit')")
    public Object update(${className} resources) {
        ${changeClassName}Service.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除${apiAlias}")
    @Operation(summary = "删除${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:del')")
    public Object delete${className}(Set<Long> ids) {
        ${changeClassName}Service.delete(ids);
        return 1;
    }
}
