package me.zhengjie.service;

import cn.hutool.core.lang.Dict;
import io.quarkus.panache.common.Page;
import me.zhengjie.domain.SysLog;
import me.zhengjie.service.dto.SysLogQueryCriteria;
import me.zhengjie.service.dto.SysLogSmallDto;
import me.zhengjie.utils.PageResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Zheng Jie
 * @since 2018-11-24
 */
public interface SysLogService {

    /**
     * 分页查询
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(SysLogQueryCriteria criteria, Page pageable);

    /**
     * 查询全部数据
     * @param criteria 查询条件
     * @return /
     */
    List<SysLog> queryAll(SysLogQueryCriteria criteria);

    /**
     * 查询用户日志
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return -
     */
    PageResult<SysLogSmallDto> queryAllByUser(SysLogQueryCriteria criteria, Page pageable);

    /**
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param ip 请求IP
     * @param sysLog 日志实体
     */
//fixme    @Async
    void save(String username, String browser, String ip, SysLog sysLog);

    /**
     * 查询异常详情
     * @param id 日志ID
     * @return Object
     */
    Dict findByErrDetail(Long id);

    /**
     * 导出日志
     * @param sysLogs 待导出的数据
     * @throws IOException /
     */
    File download(List<SysLog> sysLogs) throws IOException;

    /**
     * 删除所有错误日志
     */
    void delAllByError();

    /**
     * 删除所有INFO日志
     */
    void delAllByInfo();
}
