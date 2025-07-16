package me.zhengjie.modules.mybiz.rest;

import java.io.IOException;
import java.time.LocalDateTime;

import io.quarkus.panache.common.Page;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.mybiz.domain.NationCode;
import me.zhengjie.modules.mybiz.service.NationCodeService;
import me.zhengjie.modules.mybiz.service.dto.NationCodeDto;
import me.zhengjie.modules.mybiz.service.dto.NationCodeQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "国家编码")
@Path("/api/nationCode")
public class NationCodeController {

    @Inject
    NationCodeService nationCodeService;

    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('nationCode:list')")
    public void exportNationCode(HttpServletResponse response, NationCodeQueryCriteria criteria) throws IOException {
        nationCodeService.download(nationCodeService.queryAll(criteria), response);
    }

    @GET
    @Path
    @Operation(summary = "查询国家编码")
    @PreAuthorize("@el.check('nationCode:list')")
    public ResponseEntity<PageResult<NationCodeDto>> queryNationCode(NationCodeQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(nationCodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @POST
    @Path("")
    @Log("新增国家编码")
    @Operation(summary = "新增国家编码")
    @PreAuthorize("@el.check('nationCode:add')")
    public Object createNationCode(@Valid NationCode resources) {
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        String username = currentUser.getUsername();
        resources.setCreateBy(username);
        resources.setUpdateBy(username);
        resources.setCreateTime(LocalDateTime.now());
        resources.setUpdateTime(LocalDateTime.now());
        nationCodeService.create(resources);
        return 1;
    }

    @PUT
    @Path("")
    @Log("修改国家编码")
    @Operation(summary = "修改国家编码")
    @PreAuthorize("@el.check('nationCode:edit')")
    public Object updateNationCode(@Valid NationCode resources) {
        nationCodeService.update(resources);
        return 1;
    }

    @DELETE
    @Path("")
    @Log("删除国家编码")
    @Operation(summary = "删除国家编码")
    @PreAuthorize("@el.check('nationCode:del')")
    public Object deleteNationCode(@ApiParam(value = "传ID数组[]") Long[] ids) {
        nationCodeService.deleteAll(ids);
        return 1;
    }
}