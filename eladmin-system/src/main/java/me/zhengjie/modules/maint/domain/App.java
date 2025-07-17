package me.zhengjie.modules.maint.domain;

import java.io.Serializable;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * @author zhanghouying
 * @since 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "mnt_app")
public class App extends BaseEntity implements Serializable {

    @Id
    @Column(name = "app_id")
    @Schema(description = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "端口")
    private int port;

    @Schema(description = "上传路径")
    private String uploadPath;

    @Schema(description = "部署路径")
    private String deployPath;

    @Schema(description = "备份路径")
    private String backupPath;

    @Schema(description = "启动脚本")
    private String startScript;

    @Schema(description = "部署脚本")
    private String deployScript;

    public void copy(App source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
