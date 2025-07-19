package me.zhengjie.modules.system.rest;

import cn.vt.encrypt.BCryptUtils;
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
import me.zhengjie.config.properties.RsaProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UsersRole;
import me.zhengjie.modules.system.domain.vo.UserPassVo;
import me.zhengjie.modules.system.repository.UsersRoleRepository;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserAuthCompositeService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.VerifyService;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RsaUtils;
import me.zhengjie.utils.enums.CodeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
@Tag(name = "系统：用户管理")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/api/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    @Inject
    UserService userService;
    @Inject
    UserAuthCompositeService userAuthCompositeService;
    @Inject
    DeptService deptService;
    @Inject
    RoleService roleService;
    @Inject
    VerifyService verificationCodeService;
    @Inject
    RsaProperties rsaProperties;
    @Inject
    UsersRoleRepository usersRoleRepository;

    @Operation(summary = "导出用户数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void exportUser(UserQueryCriteria criteria) throws IOException {
//  fixme:      userService.download(userService.queryAll(criteria), response);
    }

    @Operation(summary = "查询用户")
    @POST
    @Path("/query")
    @PreAuthorize("@el.check('user:list')")
    public PageResult<UserDto> queryUser(UserQueryCriteria criteria) {
        if (Objects.nonNull(criteria.getDeptId())) {
            criteria.getDeptIds().add(criteria.getDeptId());
            // 先查找是否存在子节点
            List<Dept> data = deptService.findByPid(criteria.getDeptId());
            // 然后把子节点的ID都加入到集合中
            criteria.getDeptIds().addAll(deptService.getDeptChildren(data));
        }
        // 数据权限
        List<Long> dataScopes = userAuthCompositeService.findDataScopesByUserId(getCurrentAccountId());
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (CollectionUtils.isNotEmpty(criteria.getDeptIds()) && CollectionUtils.isNotEmpty(dataScopes)) {
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if (CollectionUtils.isNotEmpty(criteria.getDeptIds())) {
                return userService.queryWithDetail(criteria, criteria.toPageRequest());
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return userService.queryWithDetail(criteria, criteria.toPageRequest());
        }
        return PageUtil.noData();
    }

    @Log("新增用户")
    @Operation(summary = "新增用户")
    @POST
    @Path("/add")
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
    @POST
    @Path("edit")
    @PreAuthorize("@el.check('user:edit')")
    public Object updateUser(/*@Validated(User.Update.class) */User resources) throws Exception {
        checkLevel(resources);
        userService.update(resources);
        return 1;
    }

    @Log("修改用户：个人中心")
    @Operation(summary = "修改用户：个人中心")
    @POST
    @Path("center")
    public Object centerUser(/*@Validated(User.Update.class) */User resources) {
        if (!resources.getId().equals(getCurrentAccountId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
        return 1;
    }

    @Log("删除用户")
    @Operation(summary = "删除用户")
    @POST
    @Path("/delete")
    @PreAuthorize("@el.check('user:del')")
    public Object deleteUser(Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUsersId(getCurrentAccountId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
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
        String oldPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getNewPass());
        UserDto user = userService.findByName(getCurrentAccount().getEmail());
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
    @POST
    @Path("/resetPwd")
    public Object resetPwd(Set<Long> ids) {
        String pwd = BCryptUtils.hashpw("123456");
        userService.resetPwd(ids, pwd);
        return 1;
    }

    @Operation(summary = "修改头像")
    @POST
    @Path(value = "/updateAvatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Object updateUserAvatar(/*@RequestParam */MultipartFormDataInput dataInput) {
        File avatar = getFileFormItem(dataInput, "avatar");
        return userService.updateAvatar(avatar, avatar.getName(), getCurrentAccountId());
    }

    @Log("修改邮箱")
    @Operation(summary = "修改邮箱")
    @POST
    @Path(value = "/updateEmail/{code}")
    public Object updateUserEmail(@PathParam("code") String code, User user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), user.getPassword());
        UserDto userDto = userService.findByName(getCurrentAccount().getEmail());
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
        Integer currentLevel = Collections.min(roleService.findByUsersId(getCurrentAccountId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        List<UsersRole> usersRoles = usersRoleRepository.findByUserId(resources.getId());
        Set<Role> roles = usersRoles.stream().map(e -> {
            Role role = new Role();
            role.setId(e.getRoleId());
            return role;
        }).collect(Collectors.toSet());
        Integer optLevel = roleService.findByRoles(roles);
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
