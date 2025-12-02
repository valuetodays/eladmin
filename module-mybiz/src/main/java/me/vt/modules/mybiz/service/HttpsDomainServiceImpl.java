package me.vt.modules.mybiz.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import me.vt.modules.mybiz.api.dto.HttpsDomainDto;
import me.vt.modules.mybiz.domain.HttpsDomain;
import me.vt.modules.mybiz.domain.NationCode;
import me.vt.modules.mybiz.repository.HttpsDomainRepository;
import me.vt.modules.mybiz.service.dto.HttpsDomainQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.HttpsDomainMapper;
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
import java.util.Set;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@ApplicationScoped
public class HttpsDomainServiceImpl {

    @Inject
    HttpsDomainRepository httpsDomainRepository;
    @Inject
    HttpsDomainMapper httpsDomainMapper;

    public PageResult<HttpsDomainDto> queryAll(HttpsDomainQueryCriteria criteria, Page pageable) {
        //        Page<HttpsDomain> page = httpsDomainRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        //        return PageUtil.toPage(page.map(httpsDomainMapper::toDto));
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, NationCode.class);
        PanacheQuery<HttpsDomain> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = httpsDomainRepository.findAll(sort);
        } else {
            panacheQuery = httpsDomainRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }
        PanacheQuery<HttpsDomain> all = panacheQuery.page(pageable);
        List<HttpsDomainDto> list = httpsDomainMapper.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<HttpsDomainDto> queryAll(HttpsDomainQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public HttpsDomainDto findById(Long id) {
        HttpsDomain httpsDomain = httpsDomainRepository.findById(id);
        ValidationUtil.isNull(httpsDomain.getId(), "HttpsDomain", "id", id);
        return httpsDomainMapper.toDto(httpsDomain);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(HttpsDomain resources) {
        httpsDomainRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(HttpsDomain resources) {
        HttpsDomain httpsDomain = httpsDomainRepository.findById(resources.getId());
        ValidationUtil.isNull(httpsDomain.getId(), "HttpsDomain", "id", resources.getId());
        httpsDomain.copy(resources);
        httpsDomainRepository.save(httpsDomain);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            httpsDomainRepository.deleteById(id);
        }
    }

    public File download(List<HttpsDomainDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HttpsDomainDto d : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("标题", d.getTitle());
            map.put("域名", d.getDomain());
            map.put("备注", d.getRemark());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }
}
