package me.zhengjie.service.impl;

import cn.hutool.core.lang.Dict;
import cn.valuetodays.quarkus.commons.QueryPart;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
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
import me.zhengjie.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, SysLog.class);
        PanacheQuery<SysLog> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = logRepository.findAll();
        } else {
            panacheQuery = logRepository.find(hqlAndParams.getLeft(), hqlAndParams.getRight());
        }

        long count = panacheQuery.count();
        String status = "ERROR";
        if (status.equals(criteria.getLogType())) {
            return PageUtil.toPage(logErrorMapper.toDto(panacheQuery.list()), count);
        }
        return PageUtil.toPage(panacheQuery);
    }

    @Override
    public List<SysLog> queryAll(SysLogQueryCriteria criteria) {
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, SysLog.class);
        PanacheQuery<SysLog> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = logRepository.findAll();
        } else {
            panacheQuery = logRepository.find(hqlAndParams.getLeft(), hqlAndParams.getRight());
        }
        return panacheQuery.list();
    }

    @Override
    public PageResult<SysLogSmallDto> queryAllByUser(SysLogQueryCriteria criteria, Page pageable) {
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, SysLog.class);
        PanacheQuery<SysLog> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = logRepository.findAll();
        } else {
            panacheQuery = logRepository.find(hqlAndParams.getLeft(), hqlAndParams.getRight());
        }
        return PageUtil.toPage(logSmallMapper.toDto(panacheQuery.list()), panacheQuery.count());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(String username, String browser, String ip, SysLog sysLog) {
        if (sysLog == null) {
            throw new IllegalArgumentException("Log 不能为 null!");
        }

        // 填充基本信息
        sysLog.setRequestIp(ip);
        // fixme
        sysLog.setAddress("StringUtils.getCityInfo(sysLog.getRequestIp())");
//        sysLog.setAddress(StringUtils.getCityInfo(sysLog.getRequestIp()));
        sysLog.setUsername(username);
        sysLog.setBrowser(browser);

        // 保存
        logRepository.persist(sysLog);
    }


    @Override
    public Dict findByErrDetail(Long id) {
        SysLog sysLog = logRepository.findById(id);
        ValidationUtil.isNull(sysLog.getId(), "Log", "id", id);
        String exceptionDetail = sysLog.getExceptionDetail();
        return Dict.create().set("exception", StringUtils.trimToEmpty(exceptionDetail));
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
            map.put("异常详情", org.apache.commons.lang3.StringUtils.trimToEmpty(sysLog.getExceptionDetail()));
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
