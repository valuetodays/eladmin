package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
* @description S3存储实体类
* @author Zheng Jie
 * @since 2025-06-25
**/
@Data
@Entity
@Table(name = "tool_s3_storage")
@EqualsAndHashCode(callSuper = true)
public class S3Storage extends BaseEntity implements Serializable {

    @Id
    @Column(name = "storage_id")
    @Schema(description = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Schema(description = "文件名称")
    private String fileName;

    @NotBlank
    @Schema(description = "真实存储的名称")
    private String fileRealName;

    @NotBlank
    @Schema(description = "文件大小")
    private String fileSize;

    @NotBlank
    @Schema(description = "文件MIME 类型")
    private String fileMimeType;

    @NotBlank
    @Schema(description = "文件类型")
    private String fileType;

    @NotBlank
    @Schema(description = "文件路径")
    private String filePath;

    public void copy(S3Storage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
