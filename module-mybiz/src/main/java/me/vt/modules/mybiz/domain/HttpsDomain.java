package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author valuetodays
* @since 2025-12-01 22:19
**/
@Entity
@Data
@Table(name = "ex_https_domain")
public class HttpsDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "主键")
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank
    @Schema(description = "标题")
    private String title;

    @Column(name = "domain", nullable = false)
    @NotBlank
    @Schema(description = "域名")
    private String domain;

    @Column(name = "remark")
    @Schema(description = "备注")
    private String remark;

    public void copy(HttpsDomain source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
