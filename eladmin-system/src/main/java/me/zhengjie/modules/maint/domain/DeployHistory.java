package me.zhengjie.modules.maint.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Entity
@Getter
@Setter
@Table(name="mnt_deploy_history")
public class DeployHistory implements Serializable {

    @Id
    @Column(name = "history_id")
    @Schema(description = "ID", hidden = true)
    private String id;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "IP")
    private String ip;

    @CreationTimestamp
    @Schema(description = "部署时间")
    private Timestamp deployDate;

    @Schema(description = "部署者")
    private String deployUser;

    @Schema(description = "部署ID")
    private Long deployId;

    public void copy(DeployHistory source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
