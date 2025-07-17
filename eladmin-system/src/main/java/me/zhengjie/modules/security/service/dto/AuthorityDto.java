package me.zhengjie.modules.security.service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
//import org.springframework.security.core.GrantedAuthority;

/**
 * 避免序列化问题
 * @author Zheng Jie
 * @since 2018-11-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto implements Serializable {

    @Schema(description = "角色名")
    private String authority;
}
