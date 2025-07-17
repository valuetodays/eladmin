package me.zhengjie.modules.maint.rest;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import cn.vt.exception.AssertUtils;
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
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.domain.Database;
import me.zhengjie.modules.maint.service.DatabaseService;
import me.zhengjie.modules.maint.service.dto.DatabaseDto;
import me.zhengjie.modules.maint.service.dto.DatabaseQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Tag(name = "运维：数据库管理")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/database")
public class DatabaseController extends BaseController {

    private final String fileSavePath = FileUtil.getTmpDirPath() + "/";
	@Inject
	DatabaseService databaseService;

	@Operation(summary = "导出数据库数据")
	@GET
	@Path(value = "/download")
	@PreAuthorize("@el.check('database:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportDatabase(DatabaseQueryCriteria criteria) throws IOException {
        File file = databaseService.download(databaseService.queryAll(criteria));
        return super.download(file);
	}

	@Operation(summary = "查询数据库")
	@GET
    @Path("")
	@PreAuthorize("@el.check('database:list')")
    public PageResult<DatabaseDto> queryDatabase(DatabaseQueryCriteria criteria, Page pageable) {
        return databaseService.queryAll(criteria, pageable);
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
        return databaseService.testConnection(resources);
	}

	@Log("执行SQL脚本")
	@Operation(summary = "执行SQL脚本")
	@POST
	@Path(value = "/upload")
	@PreAuthorize("@el.check('database:add')")
    public Object uploadDatabase(/*MultipartFile file, HttpServletRequest request*/) throws Exception {
		/*String id = request.getParameter("id");
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
		}*/
        throw AssertUtils.create("temply comment");
	}
}
