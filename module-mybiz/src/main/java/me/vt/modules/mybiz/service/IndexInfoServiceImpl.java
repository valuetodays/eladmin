package me.vt.modules.mybiz.service;

import cn.vt.util.DateUtils;
import cn.vt.util.JsonUtils;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.IndexInfoDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.domain.NationCode;
import me.vt.modules.mybiz.repository.IndexInfoRepository;
import me.vt.modules.mybiz.service.dto.IndexInfoQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.IndexInfoMapper;
import me.vt.modules.mybiz.thirdparty.csindex.CsIndexInfoResp;
import me.vt.modules.mybiz.thirdparty.csindex.data.CsIndexInfoData;
import me.vt.utils.FileUtil;
import me.vt.utils.OkhttpUtils;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import me.vt.utils.ValidationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
* @author valutodays
* @since 2025-11-12 15:44
**/
@ApplicationScoped
@Slf4j
public class IndexInfoServiceImpl {

    @Inject
    IndexInfoRepository indexInfoRepository;
    @Inject
    IndexInfoMapper indexInfoMapper;

    public PageResult<IndexInfoDto> queryAll(IndexInfoQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, NationCode.class);
        PanacheQuery<IndexInfo> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = indexInfoRepository.findAll(sort);
        } else {
            panacheQuery = indexInfoRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }
        PanacheQuery<IndexInfo> all = panacheQuery.page(pageable);
        List<IndexInfoDto> list = indexInfoMapper.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<IndexInfoDto> queryAll(IndexInfoQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public IndexInfoDto findById(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id);
        ValidationUtil.isNull(indexInfo.getId(), "IndexInfo", "id", id);
        return indexInfoMapper.toDto(indexInfo);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(IndexInfo resources) {
        indexInfoRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(IndexInfo resources) {
        IndexInfo indexInfo = indexInfoRepository.findById(resources.getId());
        ValidationUtil.isNull(indexInfo.getId(), "IndexInfo", "id", resources.getId());
        indexInfo.copy(resources);
        indexInfoRepository.save(indexInfo);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            indexInfoRepository.deleteById(id);
        }
    }

    public File download(List<IndexInfoDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (IndexInfoDto indexInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("编号", indexInfo.getCode());
            map.put("名称", indexInfo.getName());
            map.put("区域（上海，深圳，北京，香港）", indexInfo.getRegion());
            map.put("描述", indexInfo.getDescription());
            map.put("发布日期", indexInfo.getReleaseDate());
            map.put("数据基准日期", indexInfo.getDataBaseDate());
            map.put("是否常见", indexInfo.getPopularFlag());
            map.put("建议的etf列表", indexInfo.getSuggestEtfs());
            map.put("创建时间", indexInfo.getCreateTime());
            map.put("修改日期", indexInfo.getUpdateTime());
            map.put("创建者id", indexInfo.getCreateUserId());
            map.put("更新者id", indexInfo.getUpdateUserId());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }

    /**
     * update only 10 record, because this operation should be in a transaction.
     * <p/>
     * caller should call this method many times;
     * @return true when all records are updated, otherwise false
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pair<Boolean, Long> updateMissingFieldsFromApiForTop10(long lastId) {
        List<IndexInfo> list = indexInfoRepository.findTop10ByReleaseDateNullOrderByIdAsc(lastId);
        if (CollectionUtils.isEmpty(list)) {
            return Pair.of(true, 0L);
        }
        for (IndexInfo indexInfo : list) {
            String proxyIp = "39.102.213.213";
            int proxyPort = -3128;
            try {
                Long l = updateMissingFieldsOne(indexInfo, proxyIp, proxyPort);
                if (l > 0L) {
                    lastId = l;
                }
                Thread.sleep(3000);
            } catch (Exception e) {
                log.error("error when updateMissingFieldsOne()", e);
            }
        }
        return Pair.of(false, lastId);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public Long updateMissingFieldsOne(IndexInfo indexInfo, String proxyIp, int proxyPort) {
        String code = indexInfo.getCsiCode();
        String url = "https://www.csindex.com.cn/csindex-home/indexInfo/index-basic-info/" + code;
        String respString = OkhttpUtils.doGet(url, proxyIp, proxyPort);
        log.info("respString={}", respString);
        CsIndexInfoResp csIndexInfoResp = JsonUtils.fromJson(respString, CsIndexInfoResp.class);
        if (Objects.isNull(csIndexInfoResp) || Boolean.TRUE.compareTo(csIndexInfoResp.getSuccess()) != 0) {
            return -1L;
        }
        CsIndexInfoData data = csIndexInfoResp.getData();
        log.info("data={}", data);
        boolean toUpdate = false;
        LocalDateTime publishDate = DateUtils.getDate(data.getPublishDate());
        if (Objects.nonNull(publishDate)) {
            indexInfo.setReleaseDate(publishDate.toLocalDate());
            toUpdate = true;
        }
        LocalDateTime basicDate = DateUtils.getDate(data.getBasicDate());
        if (Objects.nonNull(basicDate)) {
            indexInfo.setDataBaseDate(basicDate.toLocalDate());
        }
        BigDecimal basicIndex = data.getBasicIndex();
        if (Objects.nonNull(basicIndex)) {
            indexInfo.setDataBaseVal(basicIndex);
        }
        String indexCnDesc = data.getIndexCnDesc();
        if (StringUtils.isNotBlank(indexCnDesc)) {
            indexInfo.setDescription(indexCnDesc);
        }
        String adjFreqCn = data.getAdjFreqCn();
        if (StringUtils.isNotBlank(adjFreqCn)) {
            indexInfo.setAdjFreq(adjFreqCn);
        }
        if (toUpdate) {
            indexInfoRepository.update(indexInfo);
            return -1L;
        } else {
            log.info("all null for id: {}, code: {}", indexInfo.getId(), indexInfo.getCode());
        }
        return indexInfo.getId();
    }

    public Long saveAllDailyStat(IndexInfo req) {
        Long id = req.getId();
        // 要异步
        // 要通知
        // 要处理重复点击问题
        return id;
    }
}
