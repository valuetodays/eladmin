package me.zhengjie.modules.maint.rest;

import cn.vt.exception.AssertUtils;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.maint.domain.Deploy;
import me.zhengjie.modules.maint.domain.DeployHistory;
import me.zhengjie.modules.maint.service.DeployService;
import me.zhengjie.modules.maint.service.dto.DeployDto;
import me.zhengjie.modules.maint.service.dto.DeployQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.Set;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Tag(name = "运维：部署管理")
@RequiredArgsConstructor
@Path("/api/deploy")
public class DeployController {

    private final String fileSavePath = FileUtil.getTmpDirPath() + "/";
	@Inject
	DeployService deployService;


	@Operation(summary = "导出部署数据")
	@GET
	@Path(value = "/download")
	@PreAuthorize("@el.check('database:list')")
    public void exportDeployData(DeployQueryCriteria criteria) throws IOException {
        deployService.download(deployService.queryAll(criteria));
	}

	@Operation(summary = "查询部署")
	@GET
    @Path("")
	@PreAuthorize("@el.check('deploy:list')")
    public PageResult<DeployDto> queryDeployData(DeployQueryCriteria criteria) {
        return deployService.queryAll(criteria, criteria.toPageRequest());
    }

    @Log("新增部署")
	@Operation(summary = "新增部署")
	@POST
    @Path("add")
	@PreAuthorize("@el.check('deploy:add')")
	public Object createDeploy(@Valid Deploy resources) {
		deployService.create(resources);
		return 1;
    }

    @Log("修改部署")
	@Operation(summary = "修改部署")
    @POST
    @Path("edit")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object updateDeploy(@Valid Deploy resources) {
        deployService.update(resources);
		return 1;
    }

	@Log("删除部署")
	@Operation(summary = "删除部署")
	@DELETE
	@Path("")
	@PreAuthorize("@el.check('deploy:del')")
	public Object deleteDeploy(Set<Long> ids) {
		deployService.delete(ids);
		return 1;
	}

	@Log("上传文件部署")
	@Operation(summary = "上传文件部署")
	@POST
	@Path(value = "/upload")
	@PreAuthorize("@el.check('deploy:edit')")
    public Object uploadDeploy(/*MultipartFile file, HttpServletRequest request*/) throws Exception {
		/*Long id = Long.valueOf(request.getParameter("id"));
		String fileName = "";
		if(file != null){
			fileName = FileUtil.verifyFilename(file.getOriginalFilename());
			File deployFile = new File(fileSavePath + fileName);
			FileUtil.del(deployFile);
			file.transferTo(deployFile);
			//文件下一步要根据文件名字来
			deployService.deploy(fileSavePath + fileName ,id);
		}else{
			log.warn("没有找到相对应的文件");
		}
		Map<String,Object> map = new HashMap<>(2);
		map.put("error",0);
		map.put("id",fileName);
		return new ResponseEntity<>(map,HttpStatus.OK);*/
        throw AssertUtils.create("temply comment");
	}

	@Log("系统还原")
	@Operation(summary = "系统还原")
	@POST
	@Path(value = "/serverReduction")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object serverReduction(@Valid DeployHistory resources) {
		String result = deployService.serverReduction(resources);
        return result;
	}

	@Log("服务运行状态")
	@Operation(summary = "服务运行状态")
	@POST
	@Path(value = "/serverStatus")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object serverStatus(@Valid Deploy resources) {
		String result = deployService.serverStatus(resources);
        return result;
	}

	@Log("启动服务")
	@Operation(summary = "启动服务")
	@POST
	@Path(value = "/startServer")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object startServer(@Valid Deploy resources) {
		String result = deployService.startServer(resources);
        return result;
	}

	@Log("停止服务")
	@Operation(summary = "停止服务")
	@POST
	@Path(value = "/stopServer")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object stopServer(@Valid Deploy resources) {
		String result = deployService.stopServer(resources);
        return result;
	}
}
