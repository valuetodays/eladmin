package me.zhengjie.modules.mybiz.service;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import me.zhengjie.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author vt

 * @description 服务接口
 * @since 2025-07-11
 **/
public interface VtServerService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    PageResult<VtServerDto> queryAll(VtServerQueryCriteria criteria, Page pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<VtServerDto>
     */
    List<VtServerDto> queryAll(VtServerQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return VtServerDto
     */
    VtServerDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     */
    void create(VtServer resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(VtServer resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @throws IOException /
     */
    File download(List<VtServerDto> all) throws IOException;
}
