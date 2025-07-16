
package me.zhengjie.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Entity
@Getter
@Setter
@Table(name = "sys_log")
@NoArgsConstructor
public class SysLog implements Serializable {

    @Id
    @Column(name = "log_id")
    @Schema(description = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "操作用户")
    private String username;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "方法名")
    private String method;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "日志类型")
    private String logType;

    @Schema(description = "请求ip")
    private String requestIp;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "请求耗时")
    private Long time;

    @Schema(description = "异常详细")
    private byte[] exceptionDetail;

    /** 创建日期 */
    @CreationTimestamp
    @Schema(description = "创建日期：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
