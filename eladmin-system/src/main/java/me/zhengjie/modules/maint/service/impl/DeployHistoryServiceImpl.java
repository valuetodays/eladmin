package me.zhengjie.modules.maint.service.impl;

import cn.hutool.core.util.IdUtil;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.maint.domain.DeployHistory;
import me.zhengjie.modules.maint.repository.DeployHistoryRepository;
import me.zhengjie.modules.maint.service.DeployHistoryService;
import me.zhengjie.modules.maint.service.dto.DeployHistoryDto;
import me.zhengjie.modules.maint.service.dto.DeployHistoryQueryCriteria;
import me.zhengjie.modules.maint.service.mapstruct.DeployHistoryMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@ApplicationScoped
@RequiredArgsConstructor
public class DeployHistoryServiceImpl implements DeployHistoryService {

    @Inject
    DeployHistoryRepository deployhistoryRepository;
    @Inject
    DeployHistoryMapper deployhistoryMapper;

    @Override
    public PageResult<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria, Page pageable) {
// fixme        Page<DeployHistory> page = deployhistoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
//        return PageUtil.toPage(page.map(deployhistoryMapper::toDto));
        return null;
    }

    @Override
    public List<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria){
        // fixme       return deployhistoryMapper.toDto(deployhistoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
        return null;
    }

    @Override
    public DeployHistoryDto findById(Long id) {
        DeployHistory deployhistory = deployhistoryRepository.findById(id);
        ValidationUtil.isNull(deployhistory.getId(),"DeployHistory","id",id);
        return deployhistoryMapper.toDto(deployhistory);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(DeployHistory resources) {
        resources.setId(IdUtil.getSnowflakeNextId());
        deployhistoryRepository.save(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            deployhistoryRepository.deleteById(id);
        }
    }

    @Override
    public File download(List<DeployHistoryDto> queryAll) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeployHistoryDto deployHistoryDto : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("部署编号", deployHistoryDto.getDeployId());
            map.put("应用名称", deployHistoryDto.getAppName());
            map.put("部署IP", deployHistoryDto.getIp());
            map.put("部署时间", deployHistoryDto.getDeployDate());
            map.put("部署人员", deployHistoryDto.getDeployUser());
            list.add(map);
        }
        return FileUtil.downloadExcel(list);
    }
}
