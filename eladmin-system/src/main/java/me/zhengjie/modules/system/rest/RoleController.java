
package me.zhengjie.modules.system.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Dict;
import io.quarkus.panache.common.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.dto.RoleDto;
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "系统：角色管理")
@Path("/api/roles")
public class RoleController {

    @Inject
    RoleService roleService;

    private static final String ENTITY_NAME = "role";

    @Operation(summary = "获取单个role")
    @GET
    @Path(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<RoleDto> findRoleById(@PathParam Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "导出角色数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }

    @Operation(summary = "返回全部的角色")
    @GET
    @Path(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<List<RoleDto>> queryAllRole(){
        return new ResponseEntity<>(roleService.queryAll(),HttpStatus.OK);
    }

    @Operation(summary = "查询角色")
    @GET
    @Path
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<PageResult<RoleDto>> queryRole(RoleQueryCriteria criteria, Page pageable) {
        return new ResponseEntity<>(roleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Operation(summary = "获取用户级别")
    @GET
    @Path(value = "/level")
    public Object getRoleLevel() {
        return new ResponseEntity<>(Dict.create().set("level", getLevels(null)),HttpStatus.OK);
    }

    @Log("新增角色")
    @Operation(summary = "新增角色")
    @POST
    @Path("")
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
    @PUT
    @Path("")
    @PreAuthorize("@el.check('roles:edit')")
    public Object updateRole(@Validated(Role.Update.class) Role resources) {
        getLevels(resources.getLevel());
        roleService.update(resources);
        return 1;
    }

    @Log("修改角色菜单")
    @Operation(summary = "修改角色菜单")
    @PUT
    @Path("")(value ="/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public Object updateRoleMenu(Role resources) {
        RoleDto role = roleService.findById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources,role);
        return 1;
    }

    @Log("删除角色")
    @Operation(summary = "删除角色")
    @DELETE
    @Path("")
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
        List<Integer> levels = roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if(level != null){
            if(level < min){
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
