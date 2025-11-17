package me.vt.modules.mybiz.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import me.vt.annotation.Log;
import me.vt.common.base.BaseController;
import me.vt.modules.mybiz.api.dto.StockDailyQuoteDto;
import me.vt.modules.mybiz.domain.StockDailyQuote;
import me.vt.modules.mybiz.service.StockDailyQuoteServiceImpl;
import me.vt.modules.mybiz.service.dto.StockDailyQuoteQueryCriteria;
import me.vt.utils.PageResult;
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
public class StockDailyQuoteController extends BaseController {

    @Inject
    StockDailyQuoteServiceImpl stockDailyQuoteService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('stockDailyQuote:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(StockDailyQuoteQueryCriteria criteria) throws IOException {
        File file = stockDailyQuoteService.download(stockDailyQuoteService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询cc")
    @PreAuthorize("@el.check('stockDailyQuote:list')")
    public PageResult<StockDailyQuoteDto> query(StockDailyQuoteQueryCriteria criteria) {
        return stockDailyQuoteService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("/add")
    @Log("新增cc")
    @Operation(summary = "新增cc")
    @PreAuthorize("@el.check('stockDailyQuote:add')")
    public Object create(StockDailyQuote resources) {
        stockDailyQuoteService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改cc")
    @Operation(summary = "修改cc")
    @PreAuthorize("@el.check('stockDailyQuote:edit')")
    public Object update(StockDailyQuote resources) {
        stockDailyQuoteService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除cc")
    @Operation(summary = "删除cc")
    @PreAuthorize("@el.check('stockDailyQuote:del')")
    public Object deleteFStockDailyQuote(Set<Long> ids) {
        stockDailyQuoteService.delete(ids);
        return 1;
    }
}
