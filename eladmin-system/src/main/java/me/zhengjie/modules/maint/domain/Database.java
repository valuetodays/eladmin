package me.zhengjie.modules.maint.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Entity
@Getter
@Setter
@Table(name="mnt_database")
public class Database extends BaseEntity implements Serializable {

    @Id
    @Column(name = "db_id")
    @Schema(description = "ID", hidden = true)
    private String id;

    @Schema(description = "数据库名称")
    private String name;

    @Schema(description = "数据库连接地址")
    private String jdbcUrl;

    @Schema(description = "数据库密码")
    private String pwd;

    @Schema(description = "用户名")
    private String userName;

    public void copy(Database source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
