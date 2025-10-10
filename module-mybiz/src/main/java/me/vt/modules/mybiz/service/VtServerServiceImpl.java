package me.vt.modules.mybiz.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import me.vt.modules.mybiz.api.dto.VtServerDto;
import me.vt.modules.mybiz.domain.VtServer;
import me.vt.modules.mybiz.repository.VtServerRepository;
import me.vt.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.VtServerMapper;
import me.vt.utils.FileUtil;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import me.vt.utils.ValidationUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author vt

 * @description 服务实现
 * @since 2025-07-11
 **/
@ApplicationScoped
public class VtServerServiceImpl {

    @Inject
    VtServerRepository vtServerRepository;
    @Inject
    VtServerMapper vtServerMapper;

    public PageResult<VtServerDto> queryAll(VtServerQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, VtServer.class);
        PanacheQuery<VtServer> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = vtServerRepository.findAll(sort);
        } else {
            panacheQuery = vtServerRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }

        PanacheQuery<VtServer> all = panacheQuery.page(pageable);
        List<VtServerDto> list = vtServerMapper.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<VtServerDto> queryAll(VtServerQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    @Transactional
    public VtServerDto findById(Long id) {
        VtServer vtServer = vtServerRepository.findById(id);
        ValidationUtil.isNull(vtServer.getId(), "VtServer", "id", id);
        return vtServerMapper.toDto(vtServer);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(VtServer resources) {
        vtServerRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(VtServer resources) {
        VtServer vtServer = vtServerRepository.findById(resources.getId());
        ValidationUtil.isNull(vtServer.getId(), "VtServer", "id", resources.getId());
        vtServer.copy(resources);
        vtServerRepository.save(vtServer);
    }

    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vtServerRepository.deleteById(id);
        }
    }

    public File download(List<VtServerDto> all) throws IOException {
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
        return FileUtil.writeToExcel(list);
    }
}
