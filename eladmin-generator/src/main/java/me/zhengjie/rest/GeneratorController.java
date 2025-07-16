
package me.zhengjie.rest;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.ColumnInfo;
import me.zhengjie.domain.vo.TableInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.GenConfigService;
import me.zhengjie.service.GeneratorService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zheng Jie
 * @date 2019-01-02
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/generator")
@Tag(name = "系统：代码生成管理")
public class GeneratorController {

    @Inject
    GeneratorService generatorService;
    @Inject
    GenConfigService genConfigService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    @Operation(summary = "查询数据库数据")
    @GET
    @Path(value = "/tables/all")
    public Object queryAllTables() {
        return new ResponseEntity<>(generatorService.getTables(), HttpStatus.OK);
    }

    @Operation(summary = "查询数据库数据")
    @GET
    @Path(value = "/tables")
    public ResponseEntity<PageResult<TableInfo>> queryTables(@RequestParam(defaultValue = "") String name,
                                                             @RequestParam(defaultValue = "0")Integer page,
                                                             @RequestParam(defaultValue = "10")Integer size){
        int[] startEnd = PageUtil.transToStartEnd(page, size);
        return new ResponseEntity<>(generatorService.getTables(name,startEnd), HttpStatus.OK);
    }

    @Operation(summary = "查询字段数据")
    @GET
    @Path(value = "/columns")
    public ResponseEntity<PageResult<ColumnInfo>> queryColumns(@RequestParam String tableName){
        List<ColumnInfo> columnInfos = generatorService.getColumns(tableName);
        return new ResponseEntity<>(PageUtil.toPage(columnInfos,columnInfos.size()), HttpStatus.OK);
    }

    @Operation(summary = "保存字段数据")
    @PUT
    @Path("")
    public ResponseEntity<HttpStatus> saveColumn(List<ColumnInfo> columnInfos) {
        generatorService.save(columnInfos);
        return 1;
    }

    @Operation(summary = "同步字段数据")
    @POST
    @Path(value = "sync")
    public ResponseEntity<HttpStatus> syncColumn(List<String> tables) {
        for (String table : tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table));
        }
        return 1;
    }

    @Operation(summary = "生成代码")
    @POST
    @Path(value = "/{tableName}/{type}")
    public Object generatorCode(@PathParam String tableName, @PathParam Integer type, HttpServletRequest request, HttpServletResponse response) {
        if(!generatorEnabled && type == 0){
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        switch (type){
            // 生成代码
            case 0: generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName));
                    break;
            // 预览
            case 1: return generatorService.preview(genConfigService.find(tableName), generatorService.getColumns(tableName));
            // 打包
            case 2: generatorService.download(genConfigService.find(tableName), generatorService.getColumns(tableName), request, response);
                    break;
            default: throw new BadRequestException("没有这个选项");
        }
        return 1;
    }
}
