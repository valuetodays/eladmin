package me.zhengjie.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.SysLog;
import me.zhengjie.repository.LogRepository;
import me.zhengjie.service.SysLogService;
import me.zhengjie.service.dto.SysLogQueryCriteria;
import me.zhengjie.service.dto.SysLogSmallDto;
import me.zhengjie.service.mapstruct.LogErrorMapper;
import me.zhengjie.service.mapstruct.LogSmallMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zheng Jie
 * @since 2018-11-24
 */
@ApplicationScoped
@RequiredArgsConstructor
public class SysLogServiceImpl implements SysLogService {

    @Inject
    LogRepository logRepository;
    @Inject
    LogErrorMapper logErrorMapper;
    @Inject
    LogSmallMapper logSmallMapper;
    // 定义敏感字段常量数组
    private static final String[] SENSITIVE_KEYS = {"password"};

    @Override
    public Object queryAll(SysLogQueryCriteria criteria, Page pageable) {
        //   fixme: 条件查询    Page<SysLog> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        PanacheQuery<SysLog> paged = logRepository.findAll().page(pageable);
        long count = paged.count();
        String status = "ERROR";
        if (status.equals(criteria.getLogType())) {
            return PageUtil.toPage(logErrorMapper.toDto(paged.list()), count);
        }
        return PageUtil.toPage(paged);
    }

    @Override
    public List<SysLog> queryAll(SysLogQueryCriteria criteria) {
        //   fixme: 条件查询        return logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
        return logRepository.findAll().list();
    }

    @Override
    public PageResult<SysLogSmallDto> queryAllByUser(SysLogQueryCriteria criteria, Page pageable) {
        //   fixme: 条件查询      Page<SysLog> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        PanacheQuery<SysLog> paged = logRepository.findAll().page(pageable);
        return PageUtil.toPage(logSmallMapper.toDto(paged.list()), paged.count());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(String username, String browser, String ip, SysLog sysLog) {
        if (sysLog == null) {
            throw new IllegalArgumentException("Log 不能为 null!");
        }

        // 填充基本信息
        sysLog.setRequestIp(ip);
        sysLog.setAddress(StringUtils.getCityInfo(sysLog.getRequestIp()));
        sysLog.setUsername(username);
        sysLog.setBrowser(browser);

        // 保存
        logRepository.persist(sysLog);
    }


    @Override
    public Object findByErrDetail(Long id) {
        SysLog sysLog = logRepository.findById(id);
        ValidationUtil.isNull(sysLog.getId(), "Log", "id", id);
        byte[] details = sysLog.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }

    @Override
    public File download(List<SysLog> sysLogs) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysLog sysLog : sysLogs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", sysLog.getUsername());
            map.put("IP", sysLog.getRequestIp());
            map.put("IP来源", sysLog.getAddress());
            map.put("描述", sysLog.getDescription());
            map.put("浏览器", sysLog.getBrowser());
            map.put("请求耗时/毫秒", sysLog.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(sysLog.getExceptionDetail()) ? sysLog.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", sysLog.getCreateTime());
            list.add(map);
        }
        return FileUtil.downloadExcel(list);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delAllByError() {
        logRepository.deleteByLogType("ERROR");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delAllByInfo() {
        logRepository.deleteByLogType("INFO");
    }
}
