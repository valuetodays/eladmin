package me.zhengjie.modules.maint.service;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.maint.domain.App;
import me.zhengjie.modules.maint.service.dto.AppDto;
import me.zhengjie.modules.maint.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
public interface AppService {

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<AppDto> queryAll(AppQueryCriteria criteria, Page pageable);

    /**
     * 查询全部数据
     * @param criteria 条件
     * @return /
     */
    List<AppDto> queryAll(AppQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    AppDto findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(App resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(App resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param queryAll /
     * @throws IOException /
     */
    File download(List<AppDto> queryAll) throws IOException;
}
