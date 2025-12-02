package me.vt;

import cn.vt.R;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        String path = uriInfo != null ? uriInfo.getRequestUri().toString() : "<unknown>";
        log.error("404 - Not Found URL: {}", path);
        return Response.status(Response.Status.OK).entity(R.fail("404 not found")).build();
    }
}
