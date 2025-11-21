package me.vt.modules.mybiz.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import ll.vt.quarkus.commons.msg.IVtNatsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.IndexInfoDto;
import me.vt.modules.mybiz.api.dto.StockDailyQuoteDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.service.IndexInfoServiceImpl;
import me.vt.modules.mybiz.service.StockDailyQuoteServiceImpl;
import me.vt.modules.mybiz.service.dto.StockDailyQuoteQueryCriteria;
import me.vt.utils.PageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author valuetodays
 * @since 2025-11-17 19:01
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "股票每日行情")
@Path("/api/stockDailyQuote")
@Slf4j
public class StockDailyQuoteController extends BaseController {
    @Inject
    IndexInfoServiceImpl indexInfoService;
    @Inject
    StockDailyQuoteServiceImpl stockDailyQuoteService;
    @Inject
    IVtNatsClient vtNatsClient;

    //    @Operation(summary = "导出数据")
    //    @POST
    //    @Path(value = "/download")
    //    @PreAuthorize("@el.check('stockDailyQuote:list')")
    //    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    //    public Response export(StockDailyQuoteQueryCriteria criteria) throws IOException {
    //        File file = stockDailyQuoteService.download(stockDailyQuoteService.queryAll(criteria));
    //        return super.download(file);
    //    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询cc")
    @PreAuthorize("@el.check('stockDailyQuote:list')")
    public PageResult<StockDailyQuoteDto> query(StockDailyQuoteQueryCriteria criteria) {
        return stockDailyQuoteService.queryAll(criteria, criteria.toPageRequest());
    }

    //    @POST
    //    @Path("/add")
    //    @Log("新增cc")
    //    @Operation(summary = "新增cc")
    //    @PreAuthorize("@el.check('stockDailyQuote:add')")
    //    public Object create(StockDailyQuote resources) {
    //        stockDailyQuoteService.create(resources);
    //        return 1;
    //    }
    //
    //    @POST
    //    @Path("/edit")
    //    @Log("修改cc")
    //    @Operation(summary = "修改cc")
    //    @PreAuthorize("@el.check('stockDailyQuote:edit')")
    //    public Object update(StockDailyQuote resources) {
    //        stockDailyQuoteService.update(resources);
    //        return 1;
    //    }

    //    @POST
    //    @Path("delete")
    //    @Log("删除")
    //    @Operation(summary = "删除cc")
    //    @PreAuthorize("@el.check('stockDailyQuote:del')")
    //    public Object deleteFStockDailyQuote(Set<Long> ids) {
    //        stockDailyQuoteService.delete(ids);
    //        return 1;
    //    }

    @POST
    @Path("computeAllCciById")
    @Log("计算指定指数的所有cci值")
    @Operation(summary = "计算指定指数的所有cci值")
    @PreAuthorize("@el.check('stockDailyQuote:computeAllCciById')")
    public Long computeAllCciById(IndexInfo req) {
        Long l = stockDailyQuoteService.computeAllCciById(req);
        super.executeAsync(() -> {
            vtNatsClient.publishApplicationMessage("计算cci14完成：");
        });
        return l;
    }

    @POST
    @Path("computeLatest30DaysCci")
    @Log("计算popular指数的近30天cci值")
    @Operation(summary = "计算popular指数的近30天cci值")
    @PreAuthorize("@el.check('stockDailyQuote:computeLatest30DaysCci')")
    public Long computeLatest30DaysCci() {
        List<IndexInfoDto> popularList = indexInfoService.findPopularList();
        if (CollectionUtils.isEmpty(popularList)) {
            return 0L;
        }
        super.executeAsync(() -> {
            for (IndexInfoDto indexInfoDto : popularList) {
                try {
                    stockDailyQuoteService.computeLatest30DaysCci(indexInfoDto.getCode());
                } catch (Exception e) {
                    log.error("error when updateLatest30Days", e);
                }
            }

            vtNatsClient.publishApplicationMessage("计算cci14完成");
        });
        return (long) popularList.size();
    }
}
