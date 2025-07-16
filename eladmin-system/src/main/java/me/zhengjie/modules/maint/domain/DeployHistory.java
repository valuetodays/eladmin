
package me.zhengjie.modules.maint.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
* @author zhanghouying
* @date 2019-08-24
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
