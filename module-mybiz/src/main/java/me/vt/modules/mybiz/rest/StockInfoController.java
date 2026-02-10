package me.vt.modules.mybiz.rest;

import cn.vt.exception.AssertUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import ll.vt.quarkus.commons.msg.IVtNatsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.StockInfoDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.domain.StockInfoPersist;
import me.vt.modules.mybiz.service.StockInfoServiceImpl;
import me.vt.modules.mybiz.service.dto.StockInfoQueryCriteria;
import me.vt.utils.PageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author valutodays
 * @since 2025-11-12 15:44
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "股票信息")
@Path("/api/stockInfo")
@Slf4j
public class StockInfoController extends BaseController {

    @Inject
    StockInfoServiceImpl stockInfoService;
    @Inject
    IVtNatsClient vtNatsClient;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('stockInfo:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(StockInfoQueryCriteria criteria) throws IOException {
        throw AssertUtils.create("not support");
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询dd")
    @PreAuthorize("@el.check('stockInfo:list')")
    public PageResult<StockInfoDto> query(StockInfoQueryCriteria criteria) {
        return stockInfoService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("/add")
    @Log("新增dd")
    @Operation(summary = "新增dd")
    @PreAuthorize("@el.check('stockInfo:add')")
    public Object create(StockInfoPersist resources) {
        stockInfoService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改dd")
    @Operation(summary = "修改dd")
    @PreAuthorize("@el.check('stockInfo:edit')")
    public Object update(StockInfoPersist resources) {
        stockInfoService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除dd")
    @Operation(summary = "删除dd")
    @PreAuthorize("@el.check('stockInfo:del')")
    public Object deleteIndexInfo(Set<Long> ids) {
        return 1;
    }


    @POST
    @Path("saveAllDailyStat")
    @Log("同步所有日k数据")
    @Operation(summary = "同步所有日k数据")
    @PreAuthorize("@el.check('stockInfo:saveAllDailyStat')")
    public Object saveAllDailyStat(IndexInfo req) {
        return stockInfoService.saveAllDailyStat(req);
    }

    @POST
    @Path("saveLatest30Days")
    @Log("同步所有popular指数的近30日k数据")
    @Operation(summary = "同步所有popular指数的近30日k数据")
    @PreAuthorize("@el.check('stockInfo:saveLatest30Days')")
    public Object saveLatest30Days() {
        List<StockInfoDto> popularList = stockInfoService.findPopularList();
        if (CollectionUtils.isEmpty(popularList)) {
            return 0L;
        }
        super.executeAsync(() -> {
            for (StockInfoDto indexInfoDto : popularList) {
                try {
                    stockInfoService.updateLatest30Days(indexInfoDto);
                } catch (Exception e) {
                    log.error("error when updateLatest30Days", e);
                }
            }
            vtNatsClient.publishApplicationMessage("同步所有popular指数的近30日k数据完成");
        });
        return popularList.size();
    }
}
