package me.zhengjie;

import cn.vt.R;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        log.error("request url not found：{}", exception.getMessage(), exception);
        return Response.status(Response.Status.OK).entity(R.fail("not found")).build();
    }
}
