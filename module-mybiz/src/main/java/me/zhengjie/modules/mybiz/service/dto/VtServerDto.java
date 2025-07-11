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

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author vt
 * @website https://eladmin.vip
 * @description /
 * @date 2025-07-11
 **/
@Data
public class VtServerDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private Long name;

    @ApiModelProperty(value = "绑定的端口，外网->内网")
    private String portBindings;

    @ApiModelProperty(value = "timezone状态：1启用、0禁用")
    private Integer timeZoneEnabled;

    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "https状态：1启用、0禁用")
    private Boolean httpsEnabled;

    @ApiModelProperty(value = "镜像地址")
    private String imageName;

    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Boolean enabled;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;
}