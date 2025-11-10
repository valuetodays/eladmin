package me.vt.modules.mybiz.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.vt.common.base.BaseController;
import me.vt.annotation.Log;
import me.vt.modules.mybiz.api.dto.MetricDockerStatsDto;
import me.vt.modules.mybiz.domain.MetricDockerStats;
import me.vt.modules.mybiz.service.MetricDockerStatsServiceImpl;
import me.vt.modules.mybiz.service.dto.MetricDockerStatsQueryCriteria;
import me.vt.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author vt
 * @since 2025-09-14 20:30
 **/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "docker容器占用")
@Path("/api/metricDockerStats")
public class MetricDockerStatsController extends BaseController {

    @Inject
    MetricDockerStatsServiceImpl metricDockerStatsService;

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check('metricDockerStats:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response export(MetricDockerStatsQueryCriteria criteria) throws IOException {
        File file = metricDockerStatsService.download(metricDockerStatsService.queryAll(criteria));
        return super.download(file);
    }

    @POST
    @Path(value = "/chart")
    @Operation(summary = "查询docker容器占用(图表)")
    @PreAuthorize("@el.check('metricDockerStats:chart')")
    public List<MetricDockerStatsDto> chart() {
        return metricDockerStatsService.chart();
    }

    @POST
    @Path(value = "/query")
    @Operation(summary = "查询docker容器占用")
    @PreAuthorize("@el.check('metricDockerStats:list')")
    public PageResult<MetricDockerStatsDto> query(MetricDockerStatsQueryCriteria criteria) {
        return metricDockerStatsService.queryAll(criteria, criteria.toPageRequest());
    }

    @POST
    @Path("/add")
    @Log("新增docker容器占用")
    @Operation(summary = "新增docker容器占用")
    @PreAuthorize("@el.check('metricDockerStats:add')")
    public Object create(MetricDockerStats resources) {
        metricDockerStatsService.create(resources);
        return 1;
    }

    @POST
    @Path("/edit")
    @Log("修改docker容器占用")
    @Operation(summary = "修改docker容器占用")
    @PreAuthorize("@el.check('metricDockerStats:edit')")
    public Object update(MetricDockerStats resources) {
        metricDockerStatsService.update(resources);
        return 1;
    }

    @POST
    @Path("delete")
    @Log("删除docker容器占用")
    @Operation(summary = "删除docker容器占用")
    @PreAuthorize("@el.check('metricDockerStats:del')")
    public Object deleteMetricDockerStats(Set<Long> ids) {
        metricDockerStatsService.delete(ids);
        return 1;
    }
}
