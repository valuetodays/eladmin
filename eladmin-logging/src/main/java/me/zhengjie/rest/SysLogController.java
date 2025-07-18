package me.zhengjie.rest;

import java.io.File;
import java.io.IOException;

import cn.vt.auth.AuthUser;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.service.SysLogService;
import me.zhengjie.service.dto.SysLogQueryCriteria;
import me.zhengjie.service.dto.SysLogSmallDto;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Zheng Jie
 * @since 2018-11-24
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/logs")
@Tag(name = "系统：日志管理")
public class SysLogController extends BaseController {

    @Inject
    SysLogService sysLogService;

    @Log("导出数据")
    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check()")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportLog(SysLogQueryCriteria criteria) throws IOException {
        criteria.setLogType("INFO");
        File file = sysLogService.download(sysLogService.queryAll(criteria));
        return super.download(file);
    }

    @Log("导出错误数据")
    @Operation(summary = "导出错误数据")
    @GET
    @Path(value = "/error/download")
    @PreAuthorize("@el.check()")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportErrorLog(SysLogQueryCriteria criteria) throws IOException {
        criteria.setLogType("ERROR");
        File file = sysLogService.download(sysLogService.queryAll(criteria));
        return super.download(file);
    }

    @GET
    @Path("")
    @Operation(summary = "日志查询")
    @PreAuthorize("@el.check()")
    public Object queryLog(SysLogQueryCriteria criteria) {
        criteria.setLogType("INFO");
        return sysLogService.queryAll(criteria, criteria.toPageRequest());
    }

    @GET
    @Path(value = "/user")
    @Operation(summary = "用户日志查询")
    public PageResult<SysLogSmallDto> queryUserLog(SysLogQueryCriteria criteria) {
        criteria.setLogType("INFO");
        AuthUser currentAccount = getCurrentAccount();
        criteria.setUsername(currentAccount.getEmail());
        return sysLogService.queryAllByUser(criteria, criteria.toPageRequest());
    }

    @GET
    @Path(value = "/error")
    @Operation(summary = "错误日志查询")
    @PreAuthorize("@el.check()")
    public Object queryErrorLog(SysLogQueryCriteria criteria) {
        criteria.setLogType("ERROR");
        return sysLogService.queryAll(criteria, criteria.toPageRequest());
    }

    @GET
    @Path(value = "/error/{id}")
    @Operation(summary = "日志异常详情查询")
    @PreAuthorize("@el.check()")
    public Object queryErrorLogDetail(@PathParam("id") Long id) {
        return sysLogService.findByErrDetail(id);
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
