/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.mybiz.service;

import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import me.zhengjie.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author vt
 * @website https://eladmin.vip
 * @description 服务接口
 * @date 2025-07-11
 **/
public interface VtServerService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    PageResult<VtServerDto> queryAll(VtServerQueryCriteria criteria, Pageable pageable);

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
     * @param response /
     * @throws IOException /
     */
    void download(List<VtServerDto> all, HttpServletResponse response) throws IOException;
}