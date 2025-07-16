
package me.zhengjie.modules.maint.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import io.quarkus.panache.common.Page;
import me.zhengjie.modules.maint.domain.Database;
import me.zhengjie.modules.maint.service.dto.DatabaseDto;
import me.zhengjie.modules.maint.service.dto.DatabaseQueryCriteria;
import me.zhengjie.utils.PageResult;

/**
 * @author ZhangHouYing
 * @date 2019-08-24
 */
public interface DatabaseService {

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<DatabaseDto> queryAll(DatabaseQueryCriteria criteria, Page pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<DatabaseDto> queryAll(DatabaseQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    DatabaseDto findById(String id);

    /**
     * 创建
     * @param resources /
     */
    void create(Database resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Database resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<String> ids);

	/**
	 * 测试连接数据库
	 * @param resources /
	 * @return /
	 */
	boolean testConnection(Database resources);

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException e
     */
    void download(List<DatabaseDto> queryAll, HttpServletResponse response) throws IOException;
}
