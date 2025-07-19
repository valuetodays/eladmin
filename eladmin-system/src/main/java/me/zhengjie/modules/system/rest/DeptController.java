package me.zhengjie.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.UserAuthCompositeService;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.DeptQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "系统：部门管理")
@Path("/api/dept")
public class DeptController extends BaseController {

    @Inject
    DeptService deptService;
    @Inject
    UserAuthCompositeService userAuthCompositeService;

    private static final String ENTITY_NAME = "dept";

    @Operation(summary = "导出部门数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportDept(DeptQueryCriteria criteria) throws Exception {
        List<DeptDto> deptDtos = deptService.queryAll(criteria, false);
        File file = deptService.download(deptDtos);
        return super.download(file);
    }

    @Operation(summary = "查询部门")
    @POST
    @Path("/query")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public PageResult<DeptDto> queryDept(DeptQueryCriteria criteria) {
        List<Long> dataScopes = userAuthCompositeService.findSataScopesByUserId(getCurrentAccountId());

        List<DeptDto> depts = deptService.queryAll(criteria, true, dataScopes);
        return PageUtil.toPage(depts, depts.size());
    }

    @Operation(summary = "查询部门:根据ID获取同级与上级数据")
    @POST
    @Path("/superior")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public Object getDeptSuperior(List<Long> ids,
                                  @QueryParam("exclude") @DefaultValue("false") Boolean exclude) {
        Set<DeptDto> deptSet  = new LinkedHashSet<>();
        for (Long id : ids) {
            DeptDto deptDto = deptService.findById(id);
            List<DeptDto> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            if(exclude){
                for (DeptDto dept : depts) {
                    if(dept.getId().equals(deptDto.getPid())) {
                        dept.setSubCount(dept.getSubCount() - 1);
                    }
                }
                // 编辑部门时不显示自己以及自己下级的数据，避免出现PID数据环形问题
                depts = depts.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toList());
            }
            deptSet.addAll(depts);
        }
        return deptService.buildTree(new ArrayList<>(deptSet));
    }

    @Log("新增部门")
    @Operation(summary = "新增部门")
    @POST
    @Path("/add")
    @PreAuthorize("@el.check('dept:add')")
    public Object createDept(@Valid Dept resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        deptService.create(resources);
        return "1";
    }

    @Log("修改部门")
    @Operation(summary = "修改部门")
    @POST
    @Path("/edit")
    @PreAuthorize("@el.check('dept:edit')")
    public Object updateDept(/*@Validated(Dept.Update.class) */ Dept resources) {
        deptService.update(resources);
        return 1;
    }

    @Log("删除部门")
    @Operation(summary = "删除部门")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('dept:del')")
    public Object deleteDept(Set<Long> ids) {
        Set<DeptDto> deptDtos = new HashSet<>();
        for (Long id : ids) {
            List<Dept> deptList = deptService.findByPid(id);
            deptDtos.add(deptService.findById(id));
            if(CollectionUtil.isNotEmpty(deptList)){
                deptDtos = deptService.getDeleteDepts(deptList, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos);
        deptService.delete(deptDtos);
        return 1;
    }
}
