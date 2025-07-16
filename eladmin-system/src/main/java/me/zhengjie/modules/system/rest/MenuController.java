
package me.zhengjie.modules.system.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.vo.MenuVo;
import me.zhengjie.modules.system.service.MenuService;
import me.zhengjie.modules.system.service.dto.MenuDto;
import me.zhengjie.modules.system.service.dto.MenuQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.MenuMapper;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RequiredArgsConstructor
@Tag(name = "系统：菜单管理")
@Path("/api/menus")
public class MenuController {

    @Inject
    MenuService menuService;
    @Inject
    MenuMapper menuMapper;
    private static final String ENTITY_NAME = "menu";

    @Operation(summary = "导出菜单数据")
    @GET
    @Path(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void exportMenu(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

    @GET
    @Path(value = "/build")
    @Operation(summary = "获取前端所需菜单")
    public ResponseEntity<List<MenuVo>> buildMenus(){
        List<MenuDto> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menus = menuService.buildTree(menuDtoList);
        return new ResponseEntity<>(menuService.buildMenus(menus),HttpStatus.OK);
    }

    @Operation(summary = "返回全部的菜单")
    @GET
    @Path(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<List<MenuDto>> queryAllMenu(@RequestParam Long pid){
        return new ResponseEntity<>(menuService.getMenus(pid),HttpStatus.OK);
    }

    @Operation(summary = "根据菜单ID返回所有子节点ID，包含自身ID")
    @GET
    @Path(value = "/child")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public Object childMenu(@RequestParam Long id) {
        Set<Menu> menuSet = new HashSet<>();
        List<MenuDto> menuList = menuService.getMenus(id);
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getChildMenus(menuMapper.toEntity(menuList), menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids,HttpStatus.OK);
    }

    @GET
    @Path
    @Operation(summary = "查询菜单")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<PageResult<MenuDto>> queryMenu(MenuQueryCriteria criteria) throws Exception {
        List<MenuDto> menuDtoList = menuService.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(menuDtoList, menuDtoList.size()),HttpStatus.OK);
    }

    @Operation(summary = "查询菜单:根据ID获取同级与上级数据")
    @POST
    @Path("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<List<MenuDto>> getMenuSuperior(List<Long> ids) {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if(CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                MenuDto menuDto = menuService.findById(id);
                List<MenuDto> menuDtoList = menuService.getSuperior(menuDto, new ArrayList<>());
                for (MenuDto menu : menuDtoList) {
                    if(menu.getId().equals(menuDto.getPid())) {
                        menu.setSubCount(menu.getSubCount() - 1);
                    }
                }
                menuDtos.addAll(menuDtoList);
            }
            // 编辑菜单时不显示自己以及自己下级的数据，避免出现PID数据环形问题
            menuDtos = menuDtos.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
            return new ResponseEntity<>(menuService.buildTree(new ArrayList<>(menuDtos)),HttpStatus.OK);
        }
        return new ResponseEntity<>(menuService.getMenus(null),HttpStatus.OK);
    }

    @Log("新增菜单")
    @Operation(summary = "新增菜单")
    @POST
    @Path("")
    @PreAuthorize("@el.check('menu:add')")
    public Object createMenu(@Valid Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        menuService.create(resources);
        return 1;
    }

    @Log("修改菜单")
    @Operation(summary = "修改菜单")
    @PUT
    @Path("")
    @PreAuthorize("@el.check('menu:edit')")
    public Object updateMenu(@Validated(Menu.Update.class) Menu resources) {
        menuService.update(resources);
        return 1;
    }

    @Log("删除菜单")
    @Operation(summary = "删除菜单")
    @DELETE
    @Path("")
    @PreAuthorize("@el.check('menu:del')")
    public Object deleteMenu(Set<Long> ids) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDto> menuList = menuService.getMenus(id);
            menuSet.add(menuService.findOne(id));
            menuSet = menuService.getChildMenus(menuMapper.toEntity(menuList), menuSet);
        }
        menuService.delete(menuSet);
        return 1;
    }
}
