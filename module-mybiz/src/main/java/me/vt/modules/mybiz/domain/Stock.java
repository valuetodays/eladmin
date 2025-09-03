package me.vt.modules.mybiz.domain;

import cn.valuetodays.quarkus.commons.base.jpa.JpaCrudLongIdBasePersist;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @author vt
* @since 2025-08-11 19:56
**/
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name="fortune_stock")
public class Stock extends JpaCrudLongIdBasePersist {

    @Column(name = "code",nullable = false)
    @NotBlank
    @Schema(description = "编号")
    private String code;

    @Column(name = "region",nullable = false)
    @NotBlank
    @Schema(description = "区域")
    private String region;

    @Column(name = "name",nullable = false)
    @NotBlank
    @Schema(description = "名称")
    private String name;

    @Column(name = "remark")
    @Schema(description = "备注")
    private String remark;

    public void copy(Stock source) {
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
