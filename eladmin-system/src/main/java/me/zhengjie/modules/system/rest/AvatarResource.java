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

@Path("/avatar")
public class AvatarResource {

    @Inject
    FileProperties fileProperties;

    @GET
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAvatar(@PathParam("fileName") String fileName) {
        java.nio.file.Path avatarPath = Paths.get(fileProperties.getPath().getAvatar(), fileName);
        if (!Files.exists(avatarPath)) {
            throw new NotFoundException("Avatar not found");
        }
        return Response.ok(avatarPath.toFile())
            .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
            .build();
    }
}
