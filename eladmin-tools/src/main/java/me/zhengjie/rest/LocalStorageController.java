
package me.zhengjie.rest;

import java.io.IOException;

import io.quarkus.panache.common.Page;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.LocalStorageService;
import me.zhengjie.service.dto.LocalStorageDto;
import me.zhengjie.service.dto.LocalStorageQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Zheng Jie
* @date 2019-09-05
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "工具：本地存储管理")
@Path("/api/localStorage")
public class LocalStorageController {

    @Inject
    LocalStorageService localStorageService;

    @GET
    @Path
    @Operation(summary = "查询文件")
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<PageResult<LocalStorageDto>> queryFile(LocalStorageQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(localStorageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    @POST
    @Path("")
    @Operation(summary = "上传文件")
    @PreAuthorize("@el.check('storage:add')")
    public Object createFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        localStorageService.create(name, file);
        return 1;
    }

    @Operation(summary = "上传图片")
    @POST
    @Path("/pictures")
    public ResponseEntity<LocalStorage> uploadPicture(@RequestParam MultipartFile file){
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if(!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))){
            throw new BadRequestException("只能上传图片");
        }
        LocalStorage localStorage = localStorageService.create(null, file);
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
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