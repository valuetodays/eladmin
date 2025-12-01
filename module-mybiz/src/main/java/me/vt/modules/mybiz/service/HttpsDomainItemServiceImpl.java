package me.vt.modules.mybiz.service;

import jakarta.inject.Inject;
import me.vt.modules.mybiz.api.dto.HttpsDomainItemDto;
import me.vt.modules.mybiz.domain.HttpsDomainItem;
import me.vt.modules.mybiz.repository.HttpsDomainItemRepository;
import me.vt.modules.mybiz.service.dto.HttpsDomainItemQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.HttpsDomainItemMapper;
import me.vt.utils.ValidationUtil;
import me.vt.utils.FileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.panache.common.Page;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.Set;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.vt.utils.PageResult;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@ApplicationScoped
public class HttpsDomainItemServiceImpl {

    @Inject
    HttpsDomainItemRepository httpsDomainItemRepository;
    @Inject
    HttpsDomainItemMapper httpsDomainItemMapper;

    public PageResult<HttpsDomainItemDto> queryAll(HttpsDomainItemQueryCriteria criteria, Page pageable) {
//        Page<HttpsDomainItem> page = httpsDomainItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
//        return PageUtil.toPage(page.map(httpsDomainItemMapper::toDto));
        return null;
    }

    public List<HttpsDomainItemDto> queryAll(HttpsDomainItemQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public HttpsDomainItemDto findById(Long id) {
        HttpsDomainItem httpsDomainItem = httpsDomainItemRepository.findById(id);
        ValidationUtil.isNull(httpsDomainItem.getId(),"HttpsDomainItem","id",id);
        return httpsDomainItemMapper.toDto(httpsDomainItem);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(HttpsDomainItem resources) {
        httpsDomainItemRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(HttpsDomainItem resources) {
        HttpsDomainItem httpsDomainItem = httpsDomainItemRepository.findById(resources.getId());
        ValidationUtil.isNull( httpsDomainItem.getId(),"HttpsDomainItem","id",resources.getId());
        httpsDomainItem.copy(resources);
        httpsDomainItemRepository.save(httpsDomainItem);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            httpsDomainItemRepository.deleteById(id);
        }
    }

    public File download(List<HttpsDomainItemDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HttpsDomainItemDto httpsDomainItem : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标题", httpsDomainItem.getTitle());
            map.put("域名", httpsDomainItem.getDomain());
            map.put("备注", httpsDomainItem.getRemark());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }
}
