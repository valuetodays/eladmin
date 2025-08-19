package me.vt.modules.mybiz.rest;

import cn.vt.auth.AuthUser;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import me.vt.annotation.Log;
import me.vt.BaseController;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.service.StockServiceImpl;
import me.vt.modules.mybiz.service.dto.StockQueryCriteria;
import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.vt.utils.PageResult;
import me.vt.modules.mybiz.service.dto.StockDto;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * @author vt
 * @since 2025-08-11 19:56
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "股票信息服务")
@Path("/api/stock")
public class StockController extends BaseController {

    @Inject
    StockServiceImpl stockService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('stock:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(StockQueryCriteria criteria) throws IOException {
        File file = stockService.download(stockService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询股票信息服务")
    @PreAuthorize("@el.check('stock:list')")
    public PageResult<StockDto> query(StockQueryCriteria criteria) {
        return stockService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("/add")
    @Log("新增股票信息服务")
    @Operation(summary = "新增股票信息服务")
    @PreAuthorize("@el.check('stock:add')")
    public Object create(Stock resources) {
        // fill userId and time
        AuthUser currentUser = getCurrentAccount();
        resources.setCreateUserId(Long.valueOf(currentUser.getUserId()));
        resources.setUpdateUserId(resources.getCreateUserId());
        resources.setCreateTime(LocalDateTime.now());
        resources.setUpdateTime(LocalDateTime.now());
        stockService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改股票信息服务")
    @Operation(summary = "修改股票信息服务")
    @PreAuthorize("@el.check('stock:edit')")
    public Object update(Stock resources) {
        stockService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除股票信息服务")
    @Operation(summary = "删除股票信息服务")
    @PreAuthorize("@el.check('stock:del')")
    public Object deleteStock(Set<Long> ids) {
        stockService.delete(ids);
        return 1;
    }
}
