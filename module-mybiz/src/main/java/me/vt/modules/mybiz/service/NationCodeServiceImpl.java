package me.vt.modules.mybiz.service;

import com.vt.quarkus.commons.QueryPart;
import com.vt.quarkus.commons.base.QuerySearch;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.vt.modules.mybiz.api.dto.NationCodeDto;
import me.vt.modules.mybiz.domain.NationCode;
import me.vt.modules.mybiz.repository.NationCodeRepository;
import me.vt.modules.mybiz.service.dto.NationCodeQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.NationCodeConverter;
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
* @description 服务实现
* @author vt
* @since 2025-07-14 22:15
**/
@ApplicationScoped
public class NationCodeServiceImpl {

    @Inject
    NationCodeRepository nationCodeRepository;
    @Inject
    NationCodeConverter nationCodeConverter;

    public PageResult<NationCodeDto> queryAll(NationCodeQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, NationCode.class);
        PanacheQuery<NationCode> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = nationCodeRepository.findAll(sort);
        } else {
            panacheQuery = nationCodeRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }
        PanacheQuery<NationCode> all = panacheQuery.page(pageable);
        List<NationCodeDto> list = nationCodeConverter.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<NationCodeDto> queryAll(NationCodeQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    @Transactional
    public NationCodeDto findById(Long id) {
        NationCode nationCode = nationCodeRepository.findById(id);
        ValidationUtil.isNull(nationCode.getId(), "NationCode", "id", id);
        return nationCodeConverter.toDto(nationCode);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(NationCode resources) {
        nationCodeRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(NationCode resources) {
        NationCode nationCode = nationCodeRepository.findById(resources.getId());
        ValidationUtil.isNull(nationCode.getId(), "NationCode", "id", resources.getId());
        nationCode.copy(resources);
        nationCodeRepository.save(nationCode);
    }

    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            nationCodeRepository.deleteById(id);
        }
    }

    public File download(List<NationCodeDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NationCodeDto nationCode : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("国家编码2位", nationCode.getCode());
            map.put("国家编码3位", nationCode.getAlpha3Code());
            map.put("国家数字编码", nationCode.getNumeric());
            map.put("国家名称（英文）", nationCode.getEnglishName());
            map.put("国家名称（中文）", nationCode.getChineseName());
            map.put("货币符号", nationCode.getSymbol());
            map.put("货币", nationCode.getCurrency());
            map.put("手机区号", nationCode.getPhoneAreaCode());
            map.put("国旗", nationCode.getFlag());
            map.put("状态：1启用、0禁用", nationCode.getEnabled());
            map.put(" createTime", nationCode.getCreateTime());
            map.put(" updateTime", nationCode.getUpdateTime());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }
}
