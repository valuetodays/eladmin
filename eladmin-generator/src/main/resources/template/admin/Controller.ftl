package ${package}.rest;

import me.zhengjie.annotation.Log;
import ${package}.domain.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}QueryCriteria;
import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;

import me.zhengjie.utils.PageResult;
import ${package}.service.dto.${className}Dto;

/**
* @author ${author}
* @since ${.now?string("yyyy-MM-dd HH:mm")}
**/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "${apiAlias}")
@Path("/api/${changeClassName}")
public class ${className}Controller {

@Inject ${className}Service ${changeClassName}Service;

@Operation(summary = "导出数据")
@GET
@Path(value = "/download")
    @PreAuthorize("@el.check('${changeClassName}:list')")
public void export${className}(${className}QueryCriteria criteria) throws IOException {
        ${changeClassName}Service.download(${changeClassName}Service.queryAll(criteria), response);
    }

@GET
@Path
@Operation(summary = "查询${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:list')")
public ResponseEntity
<PageResult<${className}Dto>> query${className}(${className}QueryCriteria criteria, Page pageable){
        return new ResponseEntity<>(${changeClassName}Service.queryAll(criteria,pageable),HttpStatus.OK);
    }

@POST
@Path("")
    @Log("新增${apiAlias}")
@Operation(summary = "新增${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:add')")
public Object create${className}(@Valid  ${className} resources){
        ${changeClassName}Service.create(resources);
return 1;
    }

@PUT
@Path("")
    @Log("修改${apiAlias}")
@Operation(summary = "修改${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:edit')")
public Object update${className}(@Valid  ${className} resources){
        ${changeClassName}Service.update(resources);
return 1;
    }

@DELETE
@Path("")
    @Log("删除${apiAlias}")
@Operation(summary = "删除${apiAlias}")
    @PreAuthorize("@el.check('${changeClassName}:del')")
public Object delete${className}(@ApiParam(value = "传ID数组[]")  ${pkColumnType}[] ids) {
        ${changeClassName}Service.deleteAll(ids);
return 1;
    }
}