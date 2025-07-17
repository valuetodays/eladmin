package me.zhengjie.rest;

import io.quarkus.panache.common.Page;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.service.LocalStorageService;
import me.zhengjie.service.dto.LocalStorageDto;
import me.zhengjie.service.dto.LocalStorageQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;

/**
* @author Zheng Jie
 * @since 2019-09-05
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "工具：本地存储管理")
@Path("/api/localStorage")
public class LocalStorageController extends BaseController {

    @Inject
    LocalStorageService localStorageService;

    @GET
    @Path("")
    @Operation(summary = "查询文件")
    @PreAuthorize("@el.check('storage:list')")
    public PageResult<LocalStorageDto> queryFile(LocalStorageQueryCriteria criteria, Page pageable) {
        return localStorageService.queryAll(criteria, pageable);
    }

    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(LocalStorageQueryCriteria criteria) throws IOException {
        File file = localStorageService.download(localStorageService.queryAll(criteria));
    }

    @POST
    @Path("")
    @Operation(summary = "上传文件")
    @PreAuthorize("@el.check('storage:add')")
    public Object createFile(/*@RequestParam */String name, /*@RequestParam("file")*/ File file) {
        localStorageService.create(name, file);
        return 1;
    }

    @Operation(summary = "上传图片")
    @POST
    @Path("/pictures")
    public LocalStorage uploadPicture(/*@RequestParam*/ File file) {
        // 判断文件是否为图片
// fixme:       String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
//        if(!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))){
//            throw new BadRequestException("只能上传图片");
//        }
        LocalStorage localStorage = localStorageService.create(null, file);
        return localStorage;
    }

    @PUT
    @Path("")
    @Log("修改文件")
    @Operation(summary = "修改文件")
    @PreAuthorize("@el.check('storage:edit')")
    public Object updateFile(@Valid LocalStorage resources) {
        localStorageService.update(resources);
        return 1;
    }

    @Log("删除文件")
    @DELETE
    @Path("")
    @Operation(summary = "多选删除")
    public Object deleteFile(Long[] ids) {
        localStorageService.deleteAll(ids);
        return 1;
    }
}