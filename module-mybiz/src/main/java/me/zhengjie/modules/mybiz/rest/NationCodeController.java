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
import me.zhengjie.modules.mybiz.domain.NationCode;
import me.zhengjie.modules.mybiz.service.NationCodeService;
import me.zhengjie.modules.mybiz.service.dto.NationCodeDto;
import me.zhengjie.modules.mybiz.service.dto.NationCodeQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "国家编码")
@Path("/api/nationCode")
public class NationCodeController extends BaseController {

    @Inject
    NationCodeService nationCodeService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('nationCode:list')")
    public Response exportNationCode(NationCodeQueryCriteria criteria) throws IOException {
        File file = nationCodeService.download(nationCodeService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path("query")
    @Operation(summary = "查询国家编码")
    @PreAuthorize("@el.check('nationCode:list')")
    public PageResult<NationCodeDto> queryNationCode(NationCodeQueryCriteria criteria) {
        return nationCodeService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("save")
    @Log("新增国家编码")
    @Operation(summary = "新增国家编码")
    @PreAuthorize("@el.check('nationCode:add')")
    public Object createNationCode(@Valid NationCode resources) {
        AuthUser currentUser = getCurrentAccount();
        String username = currentUser.getEmail();
        resources.setCreateBy(username);
        resources.setUpdateBy(username);
        resources.setCreateTime(LocalDateTime.now());
        resources.setUpdateTime(LocalDateTime.now());
        nationCodeService.create(resources);
        return 1;
    }

    @POST
    @Path("edit")
    @Log("修改国家编码")
    @Operation(summary = "修改国家编码")
    @PreAuthorize("@el.check('nationCode:edit')")
    public Object updateNationCode(@Valid NationCode resources) {
        nationCodeService.update(resources);
        return 1;
    }

    @POST
    @Path("/delete")
    @Log("删除国家编码")
    @Operation(summary = "删除国家编码")
    @PreAuthorize("@el.check('nationCode:del')")
    public Object deleteNationCode(Long[] ids) {
        nationCodeService.deleteAll(ids);
        return 1;
    }
}
