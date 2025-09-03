package me.vt.modules.mybiz.service;

import cn.valuetodays.quarkus.commons.QueryPart;
import cn.valuetodays.quarkus.commons.base.QuerySearch;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.vt.modules.mybiz.api.dto.StockDto;
import me.vt.modules.mybiz.domain.Stock;
import me.vt.modules.mybiz.repository.StockRepository;
import me.vt.modules.mybiz.service.dto.StockQueryCriteria;
import me.vt.modules.mybiz.service.mapstruct.StockMapper;
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
* @author vt
* @since 2025-08-11 19:56
**/
@ApplicationScoped
public class StockServiceImpl {

    @Inject
    StockRepository stockRepository;
    @Inject
    StockMapper stockMapper;

    public PageResult<StockDto> queryAll(StockQueryCriteria criteria, Page pageable) {
        Sort sort = Sort.descending("id");
        List<QuerySearch> querySearchList = criteria.toQuerySearches();
        Pair<String, Object[]> hqlAndParams = QueryPart.toHqlAndParams(querySearchList, Stock.class);
        PanacheQuery<Stock> panacheQuery;
        if (Objects.isNull(hqlAndParams)) {
            panacheQuery = stockRepository.findAll(sort);
        } else {
            panacheQuery = stockRepository.find(hqlAndParams.getLeft(), sort, hqlAndParams.getRight());
        }

        PanacheQuery<Stock> all = panacheQuery.page(pageable);
        List<StockDto> list = stockMapper.toDto(all.list());
        return PageUtil.toPage(list, all.count());
    }

    public List<StockDto> queryAll(StockQueryCriteria criteria){
        return this.queryAll(criteria, Page.ofSize(10000)).getContent();
    }

    public StockDto findById(Long id) {
        Stock stock = stockRepository.findById(id);
        ValidationUtil.isNull(stock.getId(),"Stock","id",id);
        return stockMapper.toDto(stock);
    }

    @Transactional(rollbackOn = Exception.class)
    public void create(Stock resources) {
        stockRepository.save(resources);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(Stock resources) {
        Stock stock = stockRepository.findById(resources.getId());
        ValidationUtil.isNull( stock.getId(),"Stock","id",resources.getId());
        stock.copy(resources);
        stockRepository.save(stock);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            stockRepository.deleteById(id);
        }
    }

    public File download(List<StockDto> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockDto stock : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("编号", stock.getCode());
            map.put("区域", stock.getRegion());
            map.put("名称", stock.getName());
            map.put("备注", stock.getRemark());
            map.put("创建者", stock.getCreateBy());
            map.put("更新者", stock.getUpdateBy());
            map.put("创建日期", stock.getCreateTime());
            map.put("更新时间", stock.getUpdateTime());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }
}
