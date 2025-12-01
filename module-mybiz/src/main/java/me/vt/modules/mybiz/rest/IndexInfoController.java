package me.vt.modules.mybiz.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ll.vt.quarkus.commons.msg.IVtNatsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.IndexInfoDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.service.IndexInfoServiceImpl;
import me.vt.modules.mybiz.service.dto.IndexInfoQueryCriteria;
import me.vt.utils.PageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author valutodays
 * @since 2025-11-12 15:44
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "指数信息")
@Path("/api/indexInfo")
@Slf4j
public class IndexInfoController extends BaseController {

    @Inject
    IndexInfoServiceImpl indexInfoService;
    @Inject
    IVtNatsClient vtNatsClient;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('indexInfo:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(IndexInfoQueryCriteria criteria) throws IOException {
        File file = indexInfoService.download(indexInfoService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询dd")
    @PreAuthorize("@el.check('indexInfo:list')")
    public PageResult<IndexInfoDto> query(IndexInfoQueryCriteria criteria) {
        return indexInfoService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("/add")
    @Log("新增dd")
    @Operation(summary = "新增dd")
    @PreAuthorize("@el.check('indexInfo:add')")
    public Object create(IndexInfo resources) {
        indexInfoService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改dd")
    @Operation(summary = "修改dd")
    @PreAuthorize("@el.check('indexInfo:edit')")
    public Object update(IndexInfo resources) {
        indexInfoService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除dd")
    @Operation(summary = "删除dd")
    @PreAuthorize("@el.check('indexInfo:del')")
    public Object deleteIndexInfo(Set<Long> ids) {
        return 1;
    }

    @POST
    @Path("updateMissingFields")
    @Log("更新缺少字段")
    @Operation(summary = "更新缺少字段")
    @PreAuthorize("@el.check('indexInfo:updateMissingFields')")
    public Long updateMissingFields(IndexInfo req) {
        return indexInfoService.updateMissingFields(req);
    }

    @POST
    @Path("saveAllDailyStat")
    @Log("同步所有日k数据")
    @Operation(summary = "同步所有日k数据")
    @PreAuthorize("@el.check('indexInfo:saveAllDailyStat')")
    public Object saveAllDailyStat(IndexInfo req) {
        return indexInfoService.saveAllDailyStat(req);
    }

    @POST
    @Path("saveLatest30Days")
    @Log("同步所有popular指数的近30日k数据")
    @Operation(summary = "同步所有popular指数的近30日k数据")
    @PreAuthorize("@el.check('indexInfo:saveLatest30Days')")
    public Object saveLatest30Days() {
        List<IndexInfoDto> popularList = indexInfoService.findPopularList();
        if (CollectionUtils.isEmpty(popularList)) {
            return 0L;
        }
        super.executeAsync(() -> {
            for (IndexInfoDto indexInfoDto : popularList) {
                try {
                    indexInfoService.updateLatest30Days(indexInfoDto);
                } catch (Exception e) {
                    log.error("error when updateLatest30Days", e);
                }
            }
            vtNatsClient.publishApplicationMessage("同步所有popular指数的近30日k数据完成");
        });
        return popularList.size();
    }
}
