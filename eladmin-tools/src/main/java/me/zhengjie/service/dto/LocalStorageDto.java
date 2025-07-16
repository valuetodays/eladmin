
package me.zhengjie.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

/**
* @author Zheng Jie
* @date 2019-09-05
*/
@Getter
@Setter
public class LocalStorageDto extends BaseDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "真实文件名")
    private String realName;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "后缀")
    private String suffix;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "文件大小")
    private String size;
}