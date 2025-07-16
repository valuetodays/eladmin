
package me.zhengjie.modules.system.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.collection.CollUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.domain.DictDetail;
import me.zhengjie.modules.system.repository.DictDetailRepository;
import me.zhengjie.modules.system.repository.DictRepository;
import me.zhengjie.modules.system.service.DictDetailService;
import me.zhengjie.modules.system.service.dto.DictDetailDto;
import me.zhengjie.modules.system.service.dto.DictDetailQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.DictDetailMapper;
import me.zhengjie.utils.CacheKey;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.ValidationUtil;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@ApplicationScoped
@RequiredArgsConstructor
public class DictDetailServiceImpl implements DictDetailService {

    @Inject
    DictRepository dictRepository;
    @Inject
    DictDetailRepository dictDetailRepository;
    @Inject
    DictDetailMapper dictDetailMapper;
    @Inject
    RedisUtils redisUtils;

    @Override
    public PageResult<DictDetailDto> queryAll(DictDetailQueryCriteria criteria, Page pageable) {
        // fixme 先不用条件
        PanacheQuery<DictDetail> paged = dictDetailRepository.findAll().page(pageable);
//        Page<DictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<DictDetail> list = paged.list();
        List<DictDetailDto> dto = dictDetailMapper.toDto(list);
        return PageUtil.toPage(dto, paged.count());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(DictDetail resources) {
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = dictDetailRepository.findById(resources.getId());
        ValidationUtil.isNull( dictDetail.getId(),"DictDetail","id",resources.getId());
        resources.setId(dictDetail.getId());
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    public List<DictDetailDto> getDictByName(String name) {
        String key = CacheKey.DICT_NAME + name;
        List<DictDetail> dictDetails = redisUtils.getList(key, DictDetail.class);
        if(CollUtil.isEmpty(dictDetails)){
            dictDetails = dictDetailRepository.findByDictName(name);
            redisUtils.set(key, dictDetails, 1 , TimeUnit.DAYS);
        }
        return dictDetailMapper.toDto(dictDetails);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Long id) {
        DictDetail dictDetail = dictDetailRepository.findById(id);
        // 清理缓存
        delCaches(dictDetail);
        dictDetailRepository.deleteById(id);
    }

    public void delCaches(DictDetail dictDetail){
        Dict dict = dictRepository.findById(dictDetail.getDict().getId());
        redisUtils.del(CacheKey.DICT_NAME + dict.getName());
    }
}
