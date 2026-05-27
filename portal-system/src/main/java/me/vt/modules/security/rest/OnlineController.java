package me.vt.modules.security.rest;

import com.vt.quarkus.commons.base.PageIO;
import io.quarkus.panache.common.Page;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import me.vt.common.base.BaseController;
import me.vt.modules.security.req.OnlineUserQueryReq;
import me.vt.modules.security.service.OnlineUserService;
import me.vt.modules.security.service.dto.OnlineUserDto;
import me.vt.utils.EncryptUtils;
import me.vt.utils.PageResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Zheng Jie
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/online")
@Tag(name = "系统：在线用户管理")
public class OnlineController extends BaseController {

    @Inject
    OnlineUserService onlineUserService;

    @Operation(summary = "查询在线用户")
    @POST
    @Path("query")
    @PreAuthorize("@el.check()")
    // fixme
    public PageResult<OnlineUserDto> queryOnlineUser(OnlineUserQueryReq req) {
        return onlineUserService.getAll(req.getUsername(), Page.of(0, 10000));
    }

    @Operation(summary = "导出数据")
    @POST
    @Path(value = "/download")
    @PreAuthorize("@el.check()")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportOnlineUser(OnlineUserQueryReq req) throws IOException {
        File file = onlineUserService.download(onlineUserService.getAll(req.getUsername()));
        return super.download(file);
    }

    @Operation(summary = "踢出用户")
    @POST
    @Path("/delete")
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
