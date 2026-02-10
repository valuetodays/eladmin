package me.vt.modules.mybiz.service;

import cn.vt.exception.AssertUtils;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import ll.vt.quarkus.commons.QueryPart;
import ll.vt.quarkus.commons.base.QuerySearch;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.mybiz.api.dto.StockInfoDto;
import me.vt.modules.mybiz.domain.IndexInfo;
import me.vt.modules.mybiz.domain.NationCode;
import me.vt.modules.mybiz.domain.StockInfoPersist;
import me.vt.modules.mybiz.repository.StockInfoRepository;
import me.vt.modules.mybiz.service.dto.StockInfoQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockInfoConverter;
import me.vt.utils.PageResult;
import me.vt.utils.PageUtil;
import me.vt.utils.ValidationUtil;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author valutodays
 * @since 2025-11-12 15:44
 **/
@ApplicationScoped
@Slf4j
public class StockInfoServiceImpl {

    @Inject
    StockInfoRepository stockInfoRepository;
    @Inject
    StockInfoConverter stockInfoConverter;
    @Inject
    StockDailyQuoteServiceImpl stockDailyQuoteService;

    public PageResult<StockInfoDto> queryAll(StockInfoQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, NationCode.class);
        PanacheQuery<StockInfoPersist> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = stockInfoRepository.findAll(sort);
        } else {
            panacheQuery = stockInfoRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }
        PanacheQuery<StockInfoPersist> all = panacheQuery.page(pageable);
        List<StockInfoDto> list = stockInfoConverter.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<StockInfoDto> queryAll(StockInfoQueryCriteria criteria) {
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public StockInfoDto findById(Long id) {
        StockInfoPersist indexInfo = stockInfoRepository.findById(id);
        ValidationUtil.isNull(indexInfo.getId(), "IndexInfo", "id", id);
        return stockInfoConverter.toDto(indexInfo);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(StockInfoPersist resources) {
        stockInfoRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(StockInfoPersist resources) {
        StockInfoPersist indexInfo = stockInfoRepository.findById(resources.getId());
        ValidationUtil.isNull(indexInfo.getId(), "IndexInfo", "id", resources.getId());
        throw AssertUtils.create("not finish");
        //        indexInfoRepository.save(indexInfo);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            stockInfoRepository.deleteById(id);
        }
    }

    @Transactional
    public Long saveAllDailyStat(IndexInfo req) {
        Long id = req.getId();
        StockInfoPersist old = stockInfoRepository.findById(id);
        AssertUtils.assertNotNull(old);
        // 要异步
        stockDailyQuoteService.getAndSaveToDb(old, true);
        // 要通知
        // 要处理重复点击问题
        return id;
    }

    public List<StockInfoDto> findPopularList() {
        List<StockInfoPersist> popularList = stockInfoRepository.findPopularList();
        return stockInfoConverter.toDto(popularList);
    }

    @Transactional
    public void updateLatest30Days(StockInfoDto indexInfoDto) {
        Long id = indexInfoDto.getId();
        StockInfoPersist indexInfo = stockInfoRepository.findById(id);
        if (Objects.isNull(indexInfo)) {
            return;
        }
        stockDailyQuoteService.getAndSaveToDb(indexInfo, false);
    }
}
