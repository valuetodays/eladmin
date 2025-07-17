package me.zhengjie.modules.system.domain.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * 构建前端路由时用到
 * @author Zheng Jie
 * @since 2018-12-20
 */
@Data
public class MenuVo implements Serializable {

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "隐藏状态")
    private Boolean hidden;

    @Schema(description = "重定向")
    private String redirect;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "总是显示")
    private Boolean alwaysShow;

    @Schema(description = "元数据")
    private MenuMetaVo meta;

    @Schema(description = "子路由")
    private List<MenuVo> children;
}
