package me.zhengjie.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * @since 2025-07-11
 **/
@Entity
@Data
@Table(name = "basic_vt_server")
public class VtServer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "ID")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Column(name = "port_bindings", nullable = false)
    @NotBlank
    @Schema(description = "绑定的端口，外网->内网")
    private String portBindings;

    @Column(name = "time_zone_enabled", nullable = false)
    @NotNull
    @Schema(description = "timezone状态：1启用、0禁用")
    private Integer timeZoneEnabled;

    @Column(name = "domain", nullable = false)
    @NotBlank
    @Schema(description = "域名")
    private String domain;

    @Column(name = "https_enabled", nullable = false)
    @NotNull
    @Schema(description = "https状态：1启用、0禁用")
    private Integer httpsEnabled;

    @Column(name = "image_name", nullable = false)
    @NotBlank
    @Schema(description = "镜像地址")
    private String imageName;

    @Column(name = "enabled", nullable = false)
    @NotNull
    @Schema(description = "状态：1启用、0禁用")
    private Integer enabled;

    @Column(name = "create_by", nullable = false)
    @NotBlank
    @Schema(description = "创建者")
    private String createBy;

    @Column(name = "update_by", nullable = false)
    @NotBlank
    @Schema(description = "更新者")
    private String updateBy;

    @Column(name = "create_time", nullable = false)
    @NotNull
    @CreationTimestamp
    @Schema(description = "创建日期")
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    @NotNull
    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public void copy(VtServer source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
