package me.zhengjie.modules.mybiz.service;

import java.io.IOException;
import java.util.List;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.mybiz.domain.NationCode;
import me.zhengjie.modules.mybiz.service.dto.NationCodeDto;
import me.zhengjie.modules.mybiz.service.dto.NationCodeQueryCriteria;
import me.zhengjie.utils.PageResult;

/**
* @description 服务接口
* @author vt
* @since 2025-07-14 22:15
**/
public interface NationCodeService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<NationCodeDto> queryAll(NationCodeQueryCriteria criteria, Page pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<NationCodeDto>
    */
    List<NationCodeDto> queryAll(NationCodeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return NationCodeDto
     */
    NationCodeDto findById(Long id);

    /**
    * 创建
    * @param resources /
    */
    void create(NationCode resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(NationCode resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<NationCodeDto> all) throws IOException;
}