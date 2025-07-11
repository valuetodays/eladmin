/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.mybiz.service.impl;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vt
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2025-07-11
 **/
@Service
@RequiredArgsConstructor
public class VtServerServiceImpl implements VtServerService {

    private final VtServerRepository vtServerRepository;
    private final VtServerMapper vtServerMapper;

    @Override
    public PageResult<VtServerDto> queryAll(VtServerQueryCriteria criteria, Pageable pageable) {
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
    @Transactional(rollbackFor = Exception.class)
    public void create(VtServer resources) {
        vtServerRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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