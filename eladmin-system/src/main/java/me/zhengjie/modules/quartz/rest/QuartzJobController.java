package me.zhengjie.modules.quartz.rest;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.quartz.domain.QuartzJob;
import me.zhengjie.modules.quartz.domain.QuartzLog;
import me.zhengjie.modules.quartz.service.QuartzJobService;
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Zheng Jie
 * @since 2019-01-07
 */
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/jobs")
@Tag(name = "系统:定时任务管理")
public class QuartzJobController extends BaseController {

    private static final String ENTITY_NAME = "quartzJob";
    @Inject
    QuartzJobService quartzJobService;

    @Operation(summary = "查询定时任务")
    @GET
    @Path("")
    @PreAuthorize("@el.check('timing:list')")
    public PageResult<QuartzJob> queryQuartzJob(JobQueryCriteria criteria) {
        return quartzJobService.queryAll(criteria, criteria.toPageRequest());
    }

    @Operation(summary = "导出任务数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('timing:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportQuartzJob(JobQueryCriteria criteria) throws IOException {
        File file = quartzJobService.download(quartzJobService.queryAll(criteria));
        return super.download(file);
    }

    @Operation(summary = "导出日志数据")
    @GET
    @Path(value = "/logs/download")
    @PreAuthorize("@el.check('timing:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportQuartzJobLog(JobQueryCriteria criteria) throws IOException {
        File file = quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria));
        return super.download(file);
    }

    @Operation(summary = "查询任务执行日志")
    @GET
    @Path(value = "/logs")
    @PreAuthorize("@el.check('timing:list')")
    public PageResult<QuartzLog> queryQuartzJobLog(JobQueryCriteria criteria) {
        return quartzJobService.queryAllLog(criteria, criteria.toPageRequest());
    }

    @Log("新增定时任务")
    @Operation(summary = "新增定时任务")
    @POST
    @Path("")
    @PreAuthorize("@el.check('timing:add')")
    public Object createQuartzJob(@Valid QuartzJob resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @ApplicationScoped 定义
        checkBean(resources.getBeanName());
        quartzJobService.create(resources);
        return 1;
    }

    @Log("修改定时任务")
    @Operation(summary = "修改定时任务")
    @PUT
    @Path("")
    @PreAuthorize("@el.check('timing:edit')")
    public Object updateQuartzJob(/*@Validated(QuartzJob.Update.class)*/ QuartzJob resources) {
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @ApplicationScoped 定义
        checkBean(resources.getBeanName());
        quartzJobService.update(resources);
        return 1;
    }

    @Log("更改定时任务状态")
    @Operation(summary = "更改定时任务状态")
    @PUT
    @Path("/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public Object updateQuartzJobStatus(@PathParam("id") Long id) {
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return 1;
    }

    @Log("执行定时任务")
    @Operation(summary = "执行定时任务")
    @PUT
    @Path("/exec/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public Object executionQuartzJob(@PathParam("id") Long id) {
        quartzJobService.execution(quartzJobService.findById(id));
        return 1;
    }

    @Log("删除定时任务")
    @Operation(summary = "删除定时任务")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check('timing:del')")
    public Object deleteQuartzJob(Set<Long> ids) {
        quartzJobService.delete(ids);
        return 1;
    }

    private void checkBean(String beanName){
        // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
        // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
//  fixme:      if(!SpringBeanHolder.getAllServiceBeanName().contains(beanName)){
//            throw new BadRequestException("非法的 Bean，请重新输入！");
//        }
    }
}
