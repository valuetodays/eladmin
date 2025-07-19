package me.zhengjie.modules.system.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.service.DictService;
import me.zhengjie.modules.system.service.dto.DictDto;
import me.zhengjie.modules.system.service.dto.DictQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "系统：字典管理")
@Path("/api/dict")
public class DictController extends BaseController {

    @Inject
    DictService dictService;
    private static final String ENTITY_NAME = "dict";

    @Operation(summary = "导出字典数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportDict(DictQueryCriteria criteria) throws IOException {
        List<DictDto> queryAll = dictService.queryAll(criteria);
        File file = dictService.download(queryAll);

        return super.download(file);
    }

    @Operation(summary = "查询字典")
    @GET
    @Path(value = "/all")
    @PreAuthorize("@el.check('dict:list')")
    public List<DictDto> queryAllDict() {
        return dictService.queryAll(new DictQueryCriteria());
    }

    @Operation(summary = "查询字典")
    @GET
    @Path("")
    @PreAuthorize("@el.check('dict:list')")
    public PageResult<DictDto> queryDict(DictQueryCriteria resources) {
        return dictService.queryAll(resources, resources.toPageRequest());
    }

    @Log("新增字典")
    @Operation(summary = "新增字典")
    @POST
    @Path("add")
    @PreAuthorize("@el.check('dict:add')")
    public Object createDict(@Valid Dict resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        dictService.create(resources);
        return 1;
    }

    @Log("修改字典")
    @Operation(summary = "修改字典")
    @POST
    @Path("edit")
    @PreAuthorize("@el.check('dict:edit')")
    public Object updateDict(/*@Validated(Dict.Update.class)  */Dict resources) {
        dictService.update(resources);
        return 1;
    }

    @Log("删除字典")
    @Operation(summary = "删除字典")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('dict:del')")
    public Object deleteDict(Set<Long> ids) {
        dictService.delete(ids);
        return 1;
    }
}
