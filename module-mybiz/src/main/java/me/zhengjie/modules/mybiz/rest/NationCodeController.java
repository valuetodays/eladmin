package me.zhengjie.modules.mybiz.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.mybiz.domain.NationCode;
import me.zhengjie.modules.mybiz.service.NationCodeService;
import me.zhengjie.modules.mybiz.service.dto.NationCodeDto;
import me.zhengjie.modules.mybiz.service.dto.NationCodeQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.time.LocalDateTime;

/**
* @author vt
* @since 2025-07-14 22:15
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "国家编码")
@RequestMapping("/api/nationCode")
public class NationCodeController {

    private final NationCodeService nationCodeService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('nationCode:list')")
    public void exportNationCode(HttpServletResponse response, NationCodeQueryCriteria criteria) throws IOException {
        nationCodeService.download(nationCodeService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询国家编码")
    @PreAuthorize("@el.check('nationCode:list')")
    public ResponseEntity<PageResult<NationCodeDto>> queryNationCode(NationCodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(nationCodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增国家编码")
    @ApiOperation("新增国家编码")
    @PreAuthorize("@el.check('nationCode:add')")
    public ResponseEntity<Object> createNationCode(@Validated @RequestBody NationCode resources){
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        String username = currentUser.getUsername();
        resources.setCreateBy(username);
        resources.setUpdateBy(username);
        resources.setCreateTime(LocalDateTime.now());
        resources.setUpdateTime(LocalDateTime.now());
        nationCodeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改国家编码")
    @ApiOperation("修改国家编码")
    @PreAuthorize("@el.check('nationCode:edit')")
    public ResponseEntity<Object> updateNationCode(@Validated @RequestBody NationCode resources){
        nationCodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除国家编码")
    @ApiOperation("删除国家编码")
    @PreAuthorize("@el.check('nationCode:del')")
    public ResponseEntity<Object> deleteNationCode(@ApiParam(value = "传ID数组[]") @RequestBody Long[] ids) {
        nationCodeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}