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
package me.zhengjie.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author vt
 * @website https://eladmin.vip
 * @description /
 * @date 2025-07-11
 **/
@Entity
@Data
@Table(name = "basic_vt_server")
public class VtServer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "port_bindings", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "绑定的端口，外网->内网")
    private String portBindings;

    @Column(name = "time_zone_enabled", nullable = false)
    @NotNull
    @ApiModelProperty(value = "timezone状态：1启用、0禁用")
    private Integer timeZoneEnabled;

    @Column(name = "domain", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "域名")
    private String domain;

    @Column(name = "https_enabled", nullable = false)
    @NotNull
    @ApiModelProperty(value = "https状态：1启用、0禁用")
    private Integer httpsEnabled;

    @Column(name = "image_name", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "镜像地址")
    private String imageName;

    @Column(name = "enabled", nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Integer enabled;

    @Column(name = "create_by", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "update_by", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @Column(name = "create_time", nullable = false)
    @NotNull
    @CreationTimestamp
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    @NotNull
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    public void copy(VtServer source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
