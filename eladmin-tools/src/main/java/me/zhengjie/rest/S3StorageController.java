package me.zhengjie.rest;

import io.quarkus.panache.common.Page;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.config.AmzS3ConfigProperty;
import me.zhengjie.domain.S3Storage;
import me.zhengjie.service.S3StorageService;
import me.zhengjie.service.dto.S3StorageQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * amz S3 协议云存储管理
 * @author 郑杰
 * @since 2025-06-25
 */
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/s3Storage")
@Tag(name = "工具：S3协议云存储管理")
public class S3StorageController extends BaseController {

    @Inject
    AmzS3ConfigProperty amzS3ConfigProperty;
    @Inject
    S3StorageService s3StorageService;

    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportS3Storage(S3StorageQueryCriteria criteria) throws IOException {
        File file = s3StorageService.download(s3StorageService.queryAll(criteria));
        return super.download(file);
    }

    @GET
    @Path("")
    @Operation(summary = "查询文件")
    @PreAuthorize("@el.check('storage:list')")
    public PageResult<S3Storage> queryS3Storage(S3StorageQueryCriteria criteria, Page pageable) {
        return s3StorageService.queryAll(criteria, pageable);
    }

    @POST
    @Path("")
    @Operation(summary = "上传文件")
    public Object uploadS3Storage(File file) { // fixme:
        S3Storage storage = s3StorageService.upload(file);
        Map<String,Object> map = new HashMap<>(3);
        map.put("id",storage.getId());
        map.put("errno",0);
        map.put("data", new String[]{amzS3ConfigProperty.domain() + "/" + storage.getFilePath()});
        return map;
    }

    @Log("下载文件")
    @Operation(summary = "下载文件")
    @GET
    @Path(value = "/download/{id}")
    public Object downloadS3Storage(@PathParam("id") Long id) {
        Map<String,Object> map = new HashMap<>(1);
        S3Storage storage = s3StorageService.getById(id);
        if (storage == null) {
            map.put("message", "文件不存在或已被删除");
            return map;
        }
        // 仅适合公开文件访问，私有文件可以使用服务中的 privateDownload 方法
        String url = amzS3ConfigProperty.domain() + "/" + storage.getFilePath();
        map.put("url", url);
        return map;
    }

    @Log("删除多个文件")
    @DELETE
    @Path("")
    @Operation(summary = "删除多个文件")
    @PreAuthorize("@el.check('storage:del')")
    public Object deleteAllS3Storage(List<Long> ids) {
        s3StorageService.deleteAll(ids);
        return 1;
    }
}
