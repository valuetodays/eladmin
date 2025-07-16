
package me.zhengjie.modules.system.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.service.dto.DictDto;
import me.zhengjie.modules.system.service.dto.DictQueryCriteria;
import me.zhengjie.utils.PageResult;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
public interface DictService {

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<DictDto> queryAll(DictQueryCriteria criteria, Page pageable);

    /**
     * 查询全部数据
     * @param dict /
     * @return /
     */
    List<DictDto> queryAll(DictQueryCriteria dict);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    void create(Dict resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Dict resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    File download(List<DictDto> queryAll) throws IOException;
}
