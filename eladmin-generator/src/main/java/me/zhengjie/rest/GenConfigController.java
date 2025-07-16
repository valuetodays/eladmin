
package me.zhengjie.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.GenConfig;
import me.zhengjie.service.GenConfigService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2019-01-14
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
    public ResponseEntity<GenConfig> queryGenConfig(@PathParam String tableName) {
        return new ResponseEntity<>(genConfigService.find(tableName), HttpStatus.OK);
    }

    @PUT
    @Path("")
    @Operation(summary = "修改")
    public Object updateGenConfig(@Valid GenConfig genConfig) {
        return new ResponseEntity<>(genConfigService.update(genConfig.getTableName(), genConfig),HttpStatus.OK);
    }
}
