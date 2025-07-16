
package me.zhengjie.modules.security.rest;

import java.io.IOException;
import java.util.Set;

import io.quarkus.panache.common.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.security.service.dto.OnlineUserDto;
import me.zhengjie.utils.EncryptUtils;
import me.zhengjie.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/auth/online")
@Tag(name = "系统：在线用户管理")
public class OnlineController {

    @Inject
    OnlineUserService onlineUserService;

    @Operation(summary = "查询在线用户")
    @GET
    @Path
    @PreAuthorize("@el.check()")
    public ResponseEntity<PageResult<OnlineUserDto>> queryOnlineUser(String username, Page pageable) {
        return new ResponseEntity<>(onlineUserService.getAll(username, pageable),HttpStatus.OK);
    }

    @Operation(summary = "导出数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check()")
    public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
        onlineUserService.download(onlineUserService.getAll(username), response);
    }

    @Operation(summary = "踢出用户")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check()")
    public Object deleteOnlineUser(Set<String> keys) throws Exception {
        for (String token : keys) {
            // 解密Key
            token = EncryptUtils.desDecrypt(token);
            onlineUserService.logout(token);
        }
        return 1;
    }
}
