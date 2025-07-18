package me.zhengjie.modules.system.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Zheng Jie
 * @since 2018-12-17
 */
@Entity
@Getter
@Setter
@Table(name = "sys_menu")
public class Menu extends BaseEntity implements Serializable {

    @Id
    @Column(name = "menu_id")
    @NotNull(groups = {Update.class})
    @Schema(description = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JSONField(serialize = false)
//    @ManyToMany(mappedBy = "menus")
//    @Schema(description = "菜单角色")
//    private Set<Role> roles;

    @Schema(description = "菜单标题")
    private String title;

    @Column(name = "name")
    @Schema(description = "菜单组件名称")
    private String componentName;

    @Schema(description = "排序")
    private Integer menuSort = 999;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "菜单类型，目录、菜单、按钮")
    private Integer type;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "菜单图标")
    private String icon;

    @Column(columnDefinition = "bit(1) default 0")
    @Schema(description = "缓存")
    private Boolean cache;

    @Column(columnDefinition = "bit(1) default 0")
    @Schema(description = "是否隐藏")
    private Boolean hidden;

    @Schema(description = "上级菜单")
    private Long pid;

    @Schema(description = "子节点数目", hidden = true)
    private Integer subCount = 0;

    @Schema(description = "外链菜单")
    private Boolean iFrame;

}
