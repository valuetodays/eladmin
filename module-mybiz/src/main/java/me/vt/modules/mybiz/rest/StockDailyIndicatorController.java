package me.vt.modules.mybiz.rest;

import cn.vt.exception.AssertUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.Cci14_100DataDto;
import me.vt.modules.mybiz.api.dto.StockDailyIndicatorDto;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorGetStockToBuyByKdjReq;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorGetStockToBuyByKdjResp;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorRealtimeCciReq;
import me.vt.modules.mybiz.api.reqresp.StockDailyIndicatorRealtimeCciResp;
import me.vt.modules.mybiz.service.StockDailyIndicatorRealtimeServiceImpl;
import me.vt.modules.mybiz.service.StockDailyIndicatorServiceImpl;
import me.vt.modules.mybiz.service.dto.Cci14_100DataCriteria;
import me.vt.modules.mybiz.service.dto.StockDailyIndicatorQueryCriteria;
import me.vt.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.List;

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
    @Inject
    StockDailyIndicatorRealtimeServiceImpl stockDailyIndicatorRealtimeService;

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

    @POST
    @Path(value = "/getAllCciLt_100ByStatDate")
    @Operation(summary = "获取指定条件内cci14小于-100的数据")
    @PreAuthorize("@el.check('stockDailyIndicator:getAllCciLt_100ByStatDate')")
    public List<Cci14_100DataDto> getAllCciLt_100ByStatDate(Cci14_100DataCriteria criteria) {
        return stockDailyIndicatorService.getAllCciLt_100ByStatDate(criteria);
    }

    @POST
    @Path(value = "/getStocksToBuyByKdj")
    @Operation(summary = "根据kdj获取超买")
    @PreAuthorize("@el.check('stockDailyIndicator:getStocksToBuyByKdj')")
    public List<StockDailyIndicatorGetStockToBuyByKdjResp> getStocksToBuyByKdj(StockDailyIndicatorGetStockToBuyByKdjReq req) {
        return stockDailyIndicatorService.getStocksToBuyByKdj(req);
    }

    @POST
    @Path(value = "/realtimeCci")
    @Operation(summary = "获取实时cci")
    @PreAuthorize("@el.check('stockDailyIndicator:realtimeCci')")
    public List<StockDailyIndicatorRealtimeCciResp> realtimeCci(StockDailyIndicatorRealtimeCciReq req) {
        return stockDailyIndicatorRealtimeService.realtimeCci(req);
    }
}
