package me.zhengjie.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
 * 代码生成配置
 * @author Zheng Jie
 * @since 2019-01-03
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "code_config")
public class GenConfig implements Serializable {

    public GenConfig(String tableName) {
        this.tableName = tableName;
    }

    @Id
    @Column(name = "config_id")
    @Schema(description = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "接口名称")
    private String apiAlias;

    @NotBlank
    @Schema(description = "包路径")
    private String pack;

    @NotBlank
    @Schema(description = "模块名")
    private String moduleName;

    @NotBlank
    @Schema(description = "前端文件路径")
    private String path;

    @Schema(description = "前端文件路径")
    private String apiPath;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "表前缀")
    private String prefix;

    @Schema(description = "是否覆盖")
    private Boolean cover = false;
}
