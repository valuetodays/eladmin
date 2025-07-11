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
package me.zhengjie.modules.mybiz.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author vt
 * @website https://eladmin.vip
 * @date 2025-07-11
 **/
@Data
public class VtServerQueryCriteria {

    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "名称")
    private Long name;

    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "timezone状态：1启用、0禁用")
    private Integer timeZoneEnabled;

    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "域名")
    private String domain;

    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "https状态：1启用、0禁用")
    private Boolean httpsEnabled;

    /**
     * 模糊
     */
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty(value = "镜像地址")
    private String imageName;

    /**
     * 精确
     */
    @Query
    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Boolean enabled;
}