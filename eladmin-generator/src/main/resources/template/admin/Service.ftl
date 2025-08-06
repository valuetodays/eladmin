package ${package}.service;

import ${package}.domain.${className};
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import io.quarkus.panache.common.Page;
import java.util.Map;
import java.util.List;
import java.io.IOException;

import me.vt.utils.PageResult;

/**
* @description 服务接口
* @author ${author}
* @since ${.now?string("yyyy-MM-dd HH:mm")}
**/
public interface ${className}Service {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    */
    PageResult<${className}Dto> queryAll(${className}QueryCriteria criteria, Page pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<${className}Dto>
    */
    List<${className}Dto> queryAll(${className}QueryCriteria criteria);

    /**
     * 根据ID查询
     * @param ${pkChangeColName} ID
     * @return ${className}Dto
     */
    ${className}Dto findById(${pkColumnType} ${pkChangeColName});

    /**
    * 创建
    * @param resources /
    */
    void create(${className} resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(${className} resources);

    /**
    * 多选删除
    * @param ids /
    */
    void delete(Set<Long> ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @throws IOException /
    */
    File download(List<${className}Dto> all) throws IOException;
}
