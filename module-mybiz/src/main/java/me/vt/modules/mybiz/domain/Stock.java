package me.vt.modules.mybiz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.vt.quarkus.commons.orm.jpa.AuditableLongIdPersist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
* @author vt
* @since 2025-08-11 19:56
**/
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "fortune_stock")
public class Stock extends AuditableLongIdPersist {

    @Column(name = "code", nullable = false)
    @NotBlank
    @Schema(description = "编号")
    private String code;

    @Column(name = "region", nullable = false)
    @NotBlank
    @Schema(description = "区域")
    private String region;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Column(name = "remark")
    @Schema(description = "备注")
    private String remark;

    public void copy(Stock source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
