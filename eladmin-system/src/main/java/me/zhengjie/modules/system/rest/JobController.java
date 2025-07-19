package me.zhengjie.modules.system.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.service.JobService;
import me.zhengjie.modules.system.service.dto.JobDto;
import me.zhengjie.modules.system.service.dto.JobQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
* @author Zheng Jie
 * @since 2019-03-29
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Tag(name = "系统：岗位管理")
@Path("/api/job")
public class JobController extends BaseController {

    @Inject
    JobService jobService;
    private static final String ENTITY_NAME = "job";

    @Operation(summary = "导出岗位数据")
    @Path(value = "/download")
    @GET
    @PreAuthorize("@el.check('job:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportJob(JobQueryCriteria criteria) throws IOException {
        File file = jobService.download(jobService.queryAll(criteria));
        return super.download(file);
    }

    @Operation(summary = "查询岗位")
    @GET
    @PreAuthorize("@el.check('job:list','user:list')")
    public PageResult<JobDto> queryJob(JobQueryCriteria criteria) {
        return jobService.queryAll(criteria, criteria.toPageRequest());
    }

    @Log("新增岗位")
    @Operation(summary = "新增岗位")
    @POST
    @Path("add")
    @PreAuthorize("@el.check('job:add')")
    public Object createJob(@Valid Job resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        jobService.create(resources);
        return 1;
    }

    @Log("修改岗位")
    @Operation(summary = "修改岗位")
    @POST
    @Path("edit")
    @PreAuthorize("@el.check('job:edit')")
    public Object updateJob(/*@Validated(Job.Update.class)*/  Job resources) {
        jobService.update(resources);
        return 1;
    }

    @Log("删除岗位")
    @Operation(summary = "删除岗位")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('job:del')")
    public Object deleteJob(Set<Long> ids) {
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return 1;
    }
}
