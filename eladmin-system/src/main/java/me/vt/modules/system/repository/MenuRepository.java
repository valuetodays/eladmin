package me.vt.modules.system.repository;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import me.vt.MyPanacheRepository;
import me.vt.modules.system.domain.Menu;

import java.util.List;

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
        update("set subCount=?1 where id=?2", count, menuId);
    }

    public List<Menu> findByIdsAndTypeNotAndSortable(List<Long> menuIds, int type) {
        return find("id in ?1 and type != ?2", Sort.ascending("menuSort"), menuIds, type).list();
    }
}
