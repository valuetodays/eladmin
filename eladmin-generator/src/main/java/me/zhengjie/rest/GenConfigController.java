package me.zhengjie.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.GenConfig;
import me.zhengjie.service.GenConfigService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * @author Zheng Jie
 * @since 2019-01-14
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Path("/api/genConfig")
@Tag(name = "系统：代码生成器配置管理")
public class GenConfigController {

    @Inject
    GenConfigService genConfigService;

    @Operation(summary = "查询")
    @GET
    @Path(value = "/{tableName}")
    public GenConfig queryGenConfig(@PathParam("tableName") String tableName) {
        return genConfigService.find(tableName);
    }

    @PUT
    @Path("")
    @Operation(summary = "修改")
    public Object updateGenConfig(@Valid GenConfig genConfig) {
        return genConfigService.update(genConfig.getTableName(), genConfig);
    }
}
