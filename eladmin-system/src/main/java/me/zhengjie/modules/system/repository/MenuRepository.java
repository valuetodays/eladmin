package me.zhengjie.modules.system.repository;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Menu;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @since 2018-12-17
 */
@ApplicationScoped
public class MenuRepository extends MyPanacheRepository<Menu> {

    /**
     * 根据菜单标题查询
     * @param title 菜单标题
     * @return /
     */
    public Menu findByTitle(String title) {
        return find("title =?1", title).firstResult();
    }

    /**
     * 根据组件名称查询
     * @param name 组件名称
     * @return /
     */
    public Menu findByComponentName(String name) {
        return find("componentName =?1", name).firstResult();
    }

    /**
     * 根据菜单的 PID 查询
     * @param pid /
     * @return /
     */
    public List<Menu> findByPidOrderByMenuSort(long pid) {
        return find("pid =?1", Sort.ascending("menuSort"), pid).list();
    }

    /**
     * 查询顶级菜单
     * @return /
     */
    public List<Menu> findByPidIsNullOrderByMenuSort() {
        // todo pid is null 改为 pid=-1
        return find("pid is null", Sort.ascending("menuSort")).list();
    }

    /**
     * 根据角色ID与菜单类型查询菜单
     * @param roleIds roleIDs
     * @param type 类型
     * @return /
     */
    @Query(value = "SELECT m.* FROM sys_menu m, sys_roles_menus r WHERE " +
            "m.menu_id = r.menu_id AND r.role_id IN ?1 AND type != ?2 order by m.menu_sort asc",nativeQuery = true)
    public LinkedHashSet<Menu> findByRoleIdsAndTypeNot(Set<Long> roleIds, int type) {

    }

    /**
     * 获取节点数量
     * @param id /
     * @return /
     */
    public int countByPid(Long id) {
        return (int) count("pid=?1", id);
    }

    /**
     * 更新节点数目
     * @param count /
     * @param menuId /
     */
    @Transactional
//    @Query(value = " update sys_menu set sub_count = ?1 where menu_id = ?2 ",nativeQuery = true)
    public void updateSubCntById(int count, Long menuId) {
        update("set subCount=?1 where menuId=?2", count, menuId);
    }
}
