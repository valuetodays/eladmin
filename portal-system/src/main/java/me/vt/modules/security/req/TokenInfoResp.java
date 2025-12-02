package me.vt.modules.security.req;

import java.io.Serializable;
import lombok.Data;
import me.vt.modules.security.service.dto.JwtUserDto;

/**
 * token信息，该信息用户返回给前端，前端请求携带accessToken进行用户校验
 */
@Data
public class TokenInfoResp implements Serializable {

    private String token;
    private JwtUserDto user;

}
