package me.zhengjie;

import cn.valuetodays.quarkus.commons.base.RunAsync;
import cn.vt.R;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class DefaultExceptionHandler extends RunAsync implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        log.error("error", exception);
        String msg = exception.getMessage();
        return Response.status(Response.Status.OK).entity(R.fail(msg)).build();
    }
}
