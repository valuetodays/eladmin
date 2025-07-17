package me.zhengjie.modules.system.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.DictDetail;
import me.zhengjie.modules.system.service.DictDetailService;
import me.zhengjie.modules.system.service.dto.DictDetailDto;
import me.zhengjie.modules.system.service.dto.DictDetailQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
* @author Zheng Jie
 * @since 2019-04-10
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Tag(name = "系统：字典详情管理")
@Path("/api/dictDetail")
public class DictDetailController {

    @Inject
    DictDetailService dictDetailService;
    private static final String ENTITY_NAME = "dictDetail";

    @Operation(summary = "查询字典详情")
    @GET
    @Path("")
    public PageResult<DictDetailDto> queryDictDetail(DictDetailQueryCriteria criteria) {
        return dictDetailService.queryAll(criteria, criteria.toPageRequest());
    }

    @Operation(summary = "查询多个字典详情")
    @GET
    @Path(value = "/map")
    public Object getDictDetailMaps(@QueryParam("dictName") String dictName) {
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDto>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, dictDetailService.getDictByName(name));
        }
        return dictMap;
    }

    @Log("新增字典详情")
    @Operation(summary = "新增字典详情")
    @POST
    @Path("")
    @PreAuthorize("@el.check('dict:add')")
    public Object createDictDetail(@Valid DictDetail resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        dictDetailService.create(resources);
        return 1;
    }

    @Log("修改字典详情")
    @Operation(summary = "修改字典详情")
    @PUT
    @Path("")
    @PreAuthorize("@el.check('dict:edit')")
    public Object updateDictDetail(/*@Validated(DictDetail.Update.class) */ DictDetail resources) {
        dictDetailService.update(resources);
        return 1;
    }

    @Log("删除字典详情")
    @Operation(summary = "删除字典详情")
    @DELETE
    @Path(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public Object deleteDictDetail(@PathParam("id") Long id) {
        dictDetailService.delete(id);
        return 1;
    }
}