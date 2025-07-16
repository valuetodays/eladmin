
package me.zhengjie.modules.system.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.vt.encrypt.BCryptUtils;
import io.quarkus.panache.common.Page;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.config.properties.RsaProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.UserPassVo;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.VerifyService;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RsaUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.enums.CodeEnum;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Tag(name = "系统：用户管理")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Inject
    UserService userService;
    @Inject
    DataService dataService;
    @Inject
    DeptService deptService;
    @Inject
    RoleService roleService;
    @Inject
    VerifyService verificationCodeService;

    @Operation(summary = "导出用户数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void exportUser(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @Operation(summary = "查询用户")
    @GET
    @Path
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<PageResult<UserDto>> queryUser(UserQueryCriteria criteria, Page pageable) {
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            criteria.getDeptIds().add(criteria.getDeptId());
            // 先查找是否存在子节点
            List<Dept> data = deptService.findByPid(criteria.getDeptId());
            // 然后把子节点的ID都加入到集合中
            criteria.getDeptIds().addAll(deptService.getDeptChildren(data));
        }
        // 数据权限
        List<Long> dataScopes = dataService.getDeptIds(userService.findByName(SecurityUtils.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)){
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if(!CollectionUtil.isEmpty(criteria.getDeptIds())){
                return new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
        }
        return new ResponseEntity<>(PageUtil.noData(),HttpStatus.OK);
    }

    @Log("新增用户")
    @Operation(summary = "新增用户")
    @POST
    @Path("")
    @PreAuthorize("@el.check('user:add')")
    public Object createUser(@Valid User resources) {
        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(BCryptUtils.hashpw("123456"));
        userService.create(resources);
        return 1;
    }

    @Log("修改用户")
    @Operation(summary = "修改用户")
    @PUT
    @Path("")
    @PreAuthorize("@el.check('user:edit')")
    public Object updateUser(@Validated(User.Update.class) User resources) throws Exception {
        checkLevel(resources);
        userService.update(resources);
        return 1;
    }

    @Log("修改用户：个人中心")
    @Operation(summary = "修改用户：个人中心")
    @PUT
    @Path("center")
    public Object centerUser(@Validated(User.Update.class) User resources) {
        if(!resources.getId().equals(SecurityUtils.getCurrentUserId())){
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
        return 1;
    }

    @Log("删除用户")
    @Operation(summary = "删除用户")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check('user:del')")
    public Object deleteUser(Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel =  Collections.min(roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel =  Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.findById(id).getUsername());
            }
        }
        userService.delete(ids);
        return 1;
    }

    @Operation(summary = "修改密码")
    @POST
    @Path(value = "/updatePass")
    public Object updateUserPass(UserPassVo passVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getNewPass());
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        if (!BCryptUtils.checkpw(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (BCryptUtils.checkpw(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(user.getUsername(), BCryptUtils.hashpw(newPass));
        return 1;
    }

    @Operation(summary = "重置密码")
    @PUT
    @Path("/resetPwd")
    public Object resetPwd(Set<Long> ids) {
        String pwd = BCryptUtils.hashpw("123456");
        userService.resetPwd(ids, pwd);
        return 1;
    }

    @Operation(summary = "修改头像")
    @POST
    @Path(value = "/updateAvatar")
    public Object updateUserAvatar(@RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(userService.updateAvatar(avatar), HttpStatus.OK);
    }

    @Log("修改邮箱")
    @Operation(summary = "修改邮箱")
    @POST
    @Path(value = "/updateEmail/{code}")
    public Object updateUserEmail(@PathParam("code") String code, User user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,user.getPassword());
        UserDto userDto = userService.findByName(SecurityUtils.getCurrentUsername());
        if (!BCryptUtils.checkpw(password, userDto.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(),user.getEmail());
        return 1;
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     * @param resources /
     */
    private void checkLevel(User resources) {
        Integer currentLevel =  Collections.min(roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
