
package me.zhengjie.modules.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.collection.CollectionUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.repository.DictRepository;
import me.zhengjie.modules.system.service.DictService;
import me.zhengjie.modules.system.service.dto.DictDetailDto;
import me.zhengjie.modules.system.service.dto.DictDto;
import me.zhengjie.modules.system.service.dto.DictQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.DictMapper;
import me.zhengjie.utils.CacheKey;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.ValidationUtil;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@ApplicationScoped
public class DictServiceImpl implements DictService {

    @Inject
    DictRepository dictRepository;
    @Inject
    DictMapper dictMapper;
    @Inject
    RedisUtils redisUtils;

    @Override
    public PageResult<DictDto> queryAll(DictQueryCriteria dict, Page pageable) {
        // fixme 先不用条件
        PanacheQuery<Dict> paged = dictRepository.findAll().page(pageable);
//        Page<Dict> page = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb), pageable);
        List<Dict> list = paged.list();
        List<DictDto> dto = dictMapper.toDto(list);
        return PageUtil.toPage(dto, paged.count());
    }

    @Override
    public List<DictDto> queryAll(DictQueryCriteria dict) {
        return queryAll(dict, Page.ofSize(10000)).getContent();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(Dict resources) {
        dictRepository.save(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(Dict resources) {
        // 清理缓存
        delCaches(resources);
        Dict dict = dictRepository.findById(resources.getId());
        ValidationUtil.isNull( dict.getId(),"Dict","id",resources.getId());
        dict.setName(resources.getName());
        dict.setDescription(resources.getDescription());
        dictRepository.save(dict);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Set<Long> ids) {
        // 清理缓存
        List<Dict> dicts = dictRepository.findByIdIn(ids);
        for (Dict dict : dicts) {
            delCaches(dict);
        }
        dictRepository.deleteByIdIn(ids);
    }

    @Override
    public File download(List<DictDto> dictDtos) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDto dictDTO : dictDtos) {
            if(CollectionUtil.isNotEmpty(dictDTO.getDictDetails())){
                for (DictDetailDto dictDetail : dictDTO.getDictDetails()) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dictDTO.getName());
                    map.put("字典描述", dictDTO.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("字典名称", dictDTO.getName());
                map.put("字典描述", dictDTO.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dictDTO.getCreateTime());
                list.add(map);
            }
        }
        return FileUtil.downloadExcel(list);
    }

    public void delCaches(Dict dict){
        redisUtils.del(CacheKey.DICT_NAME + dict.getName());
    }
}
