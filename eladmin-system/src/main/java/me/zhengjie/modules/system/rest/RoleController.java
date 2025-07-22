package me.zhengjie.modules.system.rest;

import cn.hutool.core.lang.Dict;
import cn.vt.exception.CommonException;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.BaseController;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.dto.RoleDto;
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @since 2018-12-03
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "系统：角色管理")
@Path("/api/roles")
public class RoleController extends BaseController {

    @Inject
    RoleService roleService;

    private static final String ENTITY_NAME = "role";

    @Operation(summary = "获取单个role")
    @POST
    @Path(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public RoleDto findRoleById(@PathParam("id") Long id) {
        return roleService.findById(id);
    }

    @Operation(summary = "导出角色数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(RoleQueryCriteria criteria) throws IOException {
        throw new CommonException("not support");
//    fixme:    roleService.download(roleService.queryAll(criteria), response);
    }

    @Operation(summary = "返回全部的角色")
    @POST
    @Path(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public List<RoleDto> queryAllRole() {
        return roleService.queryAll();
    }

    @Operation(summary = "查询角色")
    @GET
    @Path("")
    @PreAuthorize("@el.check('roles:list')")
    public PageResult<RoleDto> queryRole(RoleQueryCriteria criteria) {
        return roleService.queryAll(criteria, criteria.toPageRequest());
    }

    @Operation(summary = "获取用户级别")
    @POST
    @Path(value = "/level")
    public Object getRoleLevel() {
        return Dict.create().set("level", getLevels(null));
    }

    @Log("新增角色")
    @Operation(summary = "新增角色")
    @POST
    @Path("add")
    @PreAuthorize("@el.check('roles:add')")
    public Object createRole(@Valid Role resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        getLevels(resources.getLevel());
        roleService.create(resources);
        return 1;
    }

    @Log("修改角色")
    @Operation(summary = "修改角色")
    @POST
    @Path("edit")
    @PreAuthorize("@el.check('roles:edit')")
    public Object updateRole(/*@Validated(Role.Update.class) */Role resources) {
        getLevels(resources.getLevel());
        roleService.update(resources);
        return 1;
    }

    @Log("修改角色菜单")
    @Operation(summary = "修改角色菜单")
    @POST
    @Path("/updateRoleMenu")
    @PreAuthorize("@el.check('roles:edit')")
    public Object updateRoleMenu(Role resources) {
        RoleDto role = roleService.findById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources,role);
        return 1;
    }

    @Log("删除角色")
    @Operation(summary = "删除角色")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('roles:del')")
    public Object deleteRole(Set<Long> ids) {
        for (Long id : ids) {
            RoleDto role = roleService.findById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.delete(ids);
        return 1;
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private int getLevels(Integer level){
        List<Integer> levels = roleService.findByUsersId(getCurrentAccountId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if(level != null){
            if(level < min){
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
