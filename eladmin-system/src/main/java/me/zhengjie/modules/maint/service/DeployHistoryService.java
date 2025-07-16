
package me.zhengjie.modules.maint.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.maint.domain.DeployHistory;
import me.zhengjie.modules.maint.service.dto.DeployHistoryDto;
import me.zhengjie.modules.maint.service.dto.DeployHistoryQueryCriteria;
import me.zhengjie.utils.PageResult;

/**
 * @author zhanghouying
 */
public interface DeployHistoryService {

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria, Page pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<DeployHistoryDto> queryAll(DeployHistoryQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    DeployHistoryDto findById(String id);

    /**
     * 创建
     * @param resources /
     */
    void create(DeployHistory resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<String> ids);

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    void download(List<DeployHistoryDto> queryAll, HttpServletResponse response) throws IOException;
}
