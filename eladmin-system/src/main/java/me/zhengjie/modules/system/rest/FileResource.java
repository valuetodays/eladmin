
package me.zhengjie.modules.system.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.zhengjie.config.properties.FileProperties;

import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/file")
public class FileResource {

    @Inject
    FileProperties fileProperties;

    @GET
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("fileName") String fileName) {
        java.nio.file.Path filePath = Paths.get(fileProperties.getPath().getPath(), fileName);
        if (!Files.exists(filePath)) {
            throw new NotFoundException("File not found");
        }
        return Response.ok(filePath.toFile())
            .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
            .build();
    }
}
