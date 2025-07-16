
package me.zhengjie.modules.maint.rest;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import io.quarkus.panache.common.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.maint.domain.Database;
import me.zhengjie.modules.maint.service.DatabaseService;
import me.zhengjie.modules.maint.service.dto.DatabaseDto;
import me.zhengjie.modules.maint.service.dto.DatabaseQueryCriteria;
import me.zhengjie.modules.maint.util.SqlUtils;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Tag(name = "运维：数据库管理")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/database")
public class DatabaseController {

	@Inject
	String fileSavePath = FileUtil.getTmpDirPath() + "/";
	@Inject
	DatabaseService databaseService;

	@Operation(summary = "导出数据库数据")
	@GET
	@Path(value = "/download")
	@PreAuthorize("@el.check('database:list')")
	public void exportDatabase(HttpServletResponse response, DatabaseQueryCriteria criteria) throws IOException {
		databaseService.download(databaseService.queryAll(criteria), response);
	}

	@Operation(summary = "查询数据库")
	@GET
	@Path
	@PreAuthorize("@el.check('database:list')")
	public ResponseEntity<PageResult<DatabaseDto>> queryDatabase(DatabaseQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(databaseService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增数据库")
	@Operation(summary = "新增数据库")
	@POST
	@Path("")
	@PreAuthorize("@el.check('database:add')")
	public Object createDatabase(@Valid Database resources) {
		databaseService.create(resources);
		return 1;
    }

    @Log("修改数据库")
	@Operation(summary = "修改数据库")
	@PUT
	@Path("")
	@PreAuthorize("@el.check('database:edit')")
	public Object updateDatabase(@Valid Database resources) {
        databaseService.update(resources);
		return 1;
    }

    @Log("删除数据库")
	@Operation(summary = "删除数据库")
	@DELETE
	@Path("")
	@PreAuthorize("@el.check('database:del')")
	public Object deleteDatabase(Set<String> ids) {
        databaseService.delete(ids);
		return 1;
    }

	@Log("测试数据库链接")
	@Operation(summary = "测试数据库链接")
	@POST
	@Path("/testConnect")
	@PreAuthorize("@el.check('database:testConnect')")
	public Object testConnect(@Valid Database resources) {
		return new ResponseEntity<>(databaseService.testConnection(resources),HttpStatus.CREATED);
	}

	@Log("执行SQL脚本")
	@Operation(summary = "执行SQL脚本")
	@POST
	@Path(value = "/upload")
	@PreAuthorize("@el.check('database:add')")
	public Object uploadDatabase(MultipartFile file, HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		DatabaseDto database = databaseService.findById(id);
		String fileName;
		if(database != null){
			fileName = FileUtil.verifyFilename(file.getOriginalFilename());
			File executeFile = new File(fileSavePath + fileName);
			FileUtil.del(executeFile);
			file.transferTo(executeFile);
			String result = SqlUtils.executeFile(database.getJdbcUrl(), database.getUserName(), database.getPwd(), executeFile);
			return new ResponseEntity<>(result,HttpStatus.OK);
		}else{
			throw new BadRequestException("Database not exist");
		}
	}
}
