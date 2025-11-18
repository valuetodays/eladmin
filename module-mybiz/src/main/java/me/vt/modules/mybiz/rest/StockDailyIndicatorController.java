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
import lombok.RequiredArgsConstructor;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.StockDailyIndicatorDto;
import me.vt.modules.mybiz.service.StockDailyIndicatorServiceImpl;
import me.vt.modules.mybiz.service.dto.StockDailyIndicatorQueryCriteria;
import me.vt.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author valuetodays
 * @since 2025-11-18 20:01
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "股票每日指标")
@Path("/api/stockDailyIndicator")
public class StockDailyIndicatorController extends BaseController {

    @Inject
    StockDailyIndicatorServiceImpl stockDailyIndicatorService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('stockDailyIndicator:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(StockDailyIndicatorQueryCriteria criteria) throws IOException {
        AssertUtils.create("not support");
        return null;
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询股票每日指标")
    @PreAuthorize("@el.check('stockDailyIndicator:list')")
    public PageResult<StockDailyIndicatorDto> query(StockDailyIndicatorQueryCriteria criteria) {
        return stockDailyIndicatorService.queryAll(criteria, criteria.toPageRequest());
    }
}
