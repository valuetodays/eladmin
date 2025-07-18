package me.zhengjie.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用字段， is_del 根据需求自行添加
 * @author Zheng Jie
 * @since 2019年10月24日20:46:32
 */
@Data
//@MappedSuperclass
//@javax.persistence.EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    //    @CreatedBy
    @Column(name = "create_by", updatable = false)
    @Schema(description = "创建人", hidden = true)
    private String createBy;

    //    @LastModifiedBy
    @Column(name = "update_by")
    @Schema(description = "更新人", hidden = true)
    private String updateBy;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间: yyyy-MM-dd HH:mm:ss", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    @Schema(description = "更新时间: yyyy-MM-dd HH:mm:ss", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /* 分组校验 */
    public @interface Create {}

    /* 分组校验 */
    public @interface Update {}

}
