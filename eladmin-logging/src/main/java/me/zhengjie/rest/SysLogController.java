
package me.zhengjie.rest;

import java.io.IOException;

import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.service.SysLogService;
import me.zhengjie.service.dto.SysLogQueryCriteria;
import me.zhengjie.service.dto.SysLogSmallDto;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/logs")
@Tag(name = "系统：日志管理")
public class SysLogController {

    @Inject
    SysLogService sysLogService;

    @Log("导出数据")
    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check()")
    public void exportLog(HttpServletResponse response, SysLogQueryCriteria criteria) throws IOException {
        criteria.setLogType("INFO");
        sysLogService.download(sysLogService.queryAll(criteria), response);
    }

    @Log("导出错误数据")
    @Operation(summary = "导出错误数据")
    @GET
    @Path(value = "/error/download")
    @PreAuthorize("@el.check()")
    public void exportErrorLog(HttpServletResponse response, SysLogQueryCriteria criteria) throws IOException {
        criteria.setLogType("ERROR");
        sysLogService.download(sysLogService.queryAll(criteria), response);
    }

    @GET
    @Path
    @Operation(summary = "日志查询")
    @PreAuthorize("@el.check()")
    public Object queryLog(SysLogQueryCriteria criteria, Page pageable) {
        criteria.setLogType("INFO");
        return new ResponseEntity<>(sysLogService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GET
    @Path(value = "/user")
    @Operation(summary = "用户日志查询")
    public ResponseEntity<PageResult<SysLogSmallDto>> queryUserLog(SysLogQueryCriteria criteria, Page pageable) {
        criteria.setLogType("INFO");
        criteria.setUsername(SecurityUtils.getCurrentUsername());
        return new ResponseEntity<>(sysLogService.queryAllByUser(criteria,pageable), HttpStatus.OK);
    }

    @GET
    @Path(value = "/error")
    @Operation(summary = "错误日志查询")
    @PreAuthorize("@el.check()")
    public Object queryErrorLog(SysLogQueryCriteria criteria, Page pageable) {
        criteria.setLogType("ERROR");
        return new ResponseEntity<>(sysLogService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GET
    @Path(value = "/error/{id}")
    @Operation(summary = "日志异常详情查询")
    @PreAuthorize("@el.check()")
    public Object queryErrorLogDetail(@PathParam Long id) {
        return new ResponseEntity<>(sysLogService.findByErrDetail(id), HttpStatus.OK);
    }

    @DELETE
    @Path(value = "/del/error")
    @Log("删除所有ERROR日志")
    @Operation(summary = "删除所有ERROR日志")
    @PreAuthorize("@el.check()")
    public Object delAllErrorLog() {
        sysLogService.delAllByError();
        return 1;
    }

    @DELETE
    @Path(value = "/del/info")
    @Log("删除所有INFO日志")
    @Operation(summary = "删除所有INFO日志")
    @PreAuthorize("@el.check()")
    public Object delAllInfoLog() {
        sysLogService.delAllByInfo();
        return 1;
    }
}
