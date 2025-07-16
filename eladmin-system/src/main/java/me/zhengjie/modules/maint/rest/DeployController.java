
package me.zhengjie.modules.maint.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import io.quarkus.panache.common.Page;
import jakarta.validation.Valid;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Slf4j
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Tag(name = "运维：部署管理")
@RequiredArgsConstructor
@Path("/api/deploy")
public class DeployController {

	@Inject
	String fileSavePath = FileUtil.getTmpDirPath() + "/";
	@Inject
	DeployService deployService;


	@Operation(summary = "导出部署数据")
	@GET
	@Path(value = "/download")
	@PreAuthorize("@el.check('database:list')")
	public void exportDeployData(HttpServletResponse response, DeployQueryCriteria criteria) throws IOException {
		deployService.download(deployService.queryAll(criteria), response);
	}

	@Operation(summary = "查询部署")
	@GET
	@Path
	@PreAuthorize("@el.check('deploy:list')")
	public ResponseEntity<PageResult<DeployDto>> queryDeployData(DeployQueryCriteria criteria, Page pageable) {
		return new ResponseEntity<>(deployService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增部署")
	@Operation(summary = "新增部署")
	@POST
	@Path("")
	@PreAuthorize("@el.check('deploy:add')")
	public Object createDeploy(@Valid Deploy resources) {
		deployService.create(resources);
		return 1;
    }

    @Log("修改部署")
	@Operation(summary = "修改部署")
	@PUT
	@Path("")
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
	public Object uploadDeploy(MultipartFile file, HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(request.getParameter("id"));
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
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

	@Log("系统还原")
	@Operation(summary = "系统还原")
	@POST
	@Path(value = "/serverReduction")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object serverReduction(@Valid DeployHistory resources) {
		String result = deployService.serverReduction(resources);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@Log("服务运行状态")
	@Operation(summary = "服务运行状态")
	@POST
	@Path(value = "/serverStatus")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object serverStatus(@Valid Deploy resources) {
		String result = deployService.serverStatus(resources);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@Log("启动服务")
	@Operation(summary = "启动服务")
	@POST
	@Path(value = "/startServer")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object startServer(@Valid Deploy resources) {
		String result = deployService.startServer(resources);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@Log("停止服务")
	@Operation(summary = "停止服务")
	@POST
	@Path(value = "/stopServer")
	@PreAuthorize("@el.check('deploy:edit')")
	public Object stopServer(@Valid Deploy resources) {
		String result = deployService.stopServer(resources);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
}
