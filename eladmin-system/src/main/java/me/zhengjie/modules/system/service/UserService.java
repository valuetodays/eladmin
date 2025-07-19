package me.zhengjie.modules.system.service;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zheng Jie
 * @since 2018-11-23
 */
public interface UserService {

    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    UserDto findById(long id);

    /**
     * 新增用户
     * @param resources /
     */
    void create(User resources);

    /**
     * 编辑用户
     * @param resources /
     * @throws Exception /
     */
    void update(User resources) throws Exception;

    /**
     * 删除用户
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    UserDto findByName(String userName);

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    UserDto getLoginData(String userName);

    /**
     * 修改密码
     * @param username 用户名
     * @param encryptPassword 密码
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     *
     * @param file             文件
     * @param currentAccountId
     * @return /
     */
    Map<String, String> updateAvatar(File file, String originalFilename, Long currentAccountId);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<UserDto> queryWithDetail(UserQueryCriteria criteria, Page pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<UserDto> queryWithDetail(UserQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    File download(List<UserDto> queryAll) throws IOException;

    /**
     * 用户自助修改资料
     * @param resources /
     */
    void updateCenter(User resources);

    /**
     * 重置密码
     * @param ids 用户id
     * @param pwd 密码
     */
    void resetPwd(Set<Long> ids, String pwd);
}
