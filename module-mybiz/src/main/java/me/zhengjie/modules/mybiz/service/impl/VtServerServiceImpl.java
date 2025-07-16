
package me.zhengjie.modules.mybiz.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.repository.VtServerRepository;
import me.zhengjie.modules.mybiz.service.VtServerService;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import me.zhengjie.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.zhengjie.modules.mybiz.service.mapstruct.VtServerMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;

/**
 * @author vt
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2025-07-11
 **/
@ApplicationScoped
@RequiredArgsConstructor
public class VtServerServiceImpl implements VtServerService {

    @Inject
    VtServerRepository vtServerRepository;
    @Inject
    VtServerMapper vtServerMapper;

    @Override
    public PageResult<VtServerDto> queryAll(VtServerQueryCriteria criteria, Page pageable) {
        Page<VtServer> page = vtServerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(vtServerMapper::toDto));
    }

    @Override
    public List<VtServerDto> queryAll(VtServerQueryCriteria criteria) {
        return vtServerMapper.toDto(vtServerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public VtServerDto findById(Long id) {
        VtServer vtServer = vtServerRepository.findById(id).orElseGet(VtServer::new);
        ValidationUtil.isNull(vtServer.getId(), "VtServer", "id", id);
        return vtServerMapper.toDto(vtServer);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(VtServer resources) {
        vtServerRepository.save(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(VtServer resources) {
        VtServer vtServer = vtServerRepository.findById(resources.getId()).orElseGet(VtServer::new);
        ValidationUtil.isNull(vtServer.getId(), "VtServer", "id", resources.getId());
        vtServer.copy(resources);
        vtServerRepository.save(vtServer);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vtServerRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VtServerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VtServerDto vtServer : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", vtServer.getName());
            map.put("绑定的端口，外网->内网", vtServer.getPortBindings());
            map.put("timezone状态：1启用、0禁用", vtServer.getTimeZoneEnabled());
            map.put("域名", vtServer.getDomain());
            map.put("https状态：1启用、0禁用", vtServer.getHttpsEnabled());
            map.put("镜像地址", vtServer.getImageName());
            map.put("状态：1启用、0禁用", vtServer.getEnabled());
            map.put("创建者", vtServer.getCreateBy());
            map.put("更新者", vtServer.getUpdateBy());
            map.put("创建日期", vtServer.getCreateTime());
            map.put("更新时间", vtServer.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}