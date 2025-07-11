/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.mybiz.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.mybiz.domain.VtServer;
import me.zhengjie.modules.mybiz.service.VtServerService;
import me.zhengjie.modules.mybiz.service.dto.VtServerDto;
import me.zhengjie.modules.mybiz.service.dto.VtServerQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vt
 * @website https://eladmin.vip
 * @date 2025-07-11
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "VtServerController")
@RequestMapping("/api/vtServer")
public class VtServerController {

    private final VtServerService vtServerService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vtServer:list')")
    public void exportVtServer(HttpServletResponse response, VtServerQueryCriteria criteria) throws IOException {
        vtServerService.download(vtServerService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询VtServerController")
    @PreAuthorize("@el.check('vtServer:list')")
    public ResponseEntity<PageResult<VtServerDto>> queryVtServer(VtServerQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(vtServerService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VtServerController")
    @ApiOperation("新增VtServerController")
    @PreAuthorize("@el.check('vtServer:add')")
    public ResponseEntity<Object> createVtServer(@Validated @RequestBody VtServer resources) {
        vtServerService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VtServerController")
    @ApiOperation("修改VtServerController")
    @PreAuthorize("@el.check('vtServer:edit')")
    public ResponseEntity<Object> updateVtServer(@Validated @RequestBody VtServer resources) {
        vtServerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除VtServerController")
    @ApiOperation("删除VtServerController")
    @PreAuthorize("@el.check('vtServer:del')")
    public ResponseEntity<Object> deleteVtServer(@ApiParam(value = "传ID数组[]") @RequestBody Long[] ids) {
        vtServerService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}