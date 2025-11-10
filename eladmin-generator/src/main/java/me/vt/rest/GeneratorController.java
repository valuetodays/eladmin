package me.vt.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.vt.common.base.BaseController;
import me.vt.domain.ColumnInfo;
import me.vt.domain.vo.TableInfo;
import me.vt.exception.BadRequestException;
import me.vt.reqresp.GenQueryColumnsReq;
import me.vt.reqresp.GenQueryTablesReq;
import me.vt.service.client.GenConfigService;
import me.vt.service.client.GeneratorService;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.io.File;
import java.util.List;

/**
 * @author Zheng Jie
 * @since 2019-01-02
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/generator")
@Tag(name = "系统：代码生成管理")
public class GeneratorController extends BaseController {

    @Inject
    GeneratorService generatorService;
    @Inject
    GenConfigService genConfigService;

    @ConfigProperty(name = "generator.enabled")
    private Boolean generatorEnabled;

    @Operation(summary = "查询数据库数据")
    @POST
    @Path(value = "/tables/all")
    public Object queryAllTables() {
        return generatorService.getTables();
    }

    @Operation(summary = "查询数据库数据")
    @POST
    @Path(value = "/tables")
    public PageResult<TableInfo> queryTables(GenQueryTablesReq req) {
        int[] startEnd = new int[]{(req.getPageNum() - 1) * req.getPageSize(), (req.getPageNum()) * req.getPageSize()};
        return generatorService.getTables(req.getName(), startEnd);
    }

    @Operation(summary = "查询字段数据")
    @POST
    @Path(value = "/columns")
    public PageResult<ColumnInfo> queryColumns(GenQueryColumnsReq req) {
        List<ColumnInfo> columnInfos = generatorService.getColumns(req.getTableName());
        return PageUtil.toPage(columnInfos, columnInfos.size());
    }

    @Operation(summary = "保存字段数据")
    @POST
    @Path("save")
    public Object saveColumn(List<ColumnInfo> columnInfos) {
        generatorService.save(columnInfos);
        return 1;
    }

    @Operation(summary = "同步字段数据")
    @POST
    @Path(value = "sync")
    public Object syncColumn(List<String> tables) {
        for (String table : tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table));
        }
        return 1;
    }

    @Operation(summary = "生成代码")
    @POST
    @Path(value = "/{tableName}/{type}")
    public Object generatorCode(@PathParam("tableName") String tableName, @PathParam("type") Integer type) {
        if (!generatorEnabled && type == 0) {
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        switch (type) {
            // 生成代码
            case 0:
                generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName));
                break;
            // 预览
            case 1:
                return generatorService.preview(genConfigService.find(tableName), generatorService.getColumns(tableName));
            default:
                throw new BadRequestException("没有这个选项");
        }
        return 1;
    }

    @Operation(summary = "生成代码")
    @POST
    @Path(value = "downloadZip/{tableName}/{type}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadZip(@PathParam("tableName") String tableName, @PathParam("type") Integer type) {
        if (!generatorEnabled && type == 0) {
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        File file = generatorService.download(genConfigService.find(tableName), generatorService.getColumns(tableName));
        return super.download(file);
    }

}
