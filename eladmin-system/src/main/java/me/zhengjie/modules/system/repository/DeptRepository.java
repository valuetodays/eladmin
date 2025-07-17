package me.zhengjie.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.modules.system.domain.Dept;

import java.util.List;
import java.util.Set;

/**
* @author Zheng Jie
 * @since 2019-03-25
*/
@ApplicationScoped
public class DeptRepository extends MyPanacheRepository<Dept> {
    /**
     * 根据 PID 查询
     * @param id pid
     * @return /
     */
    public List<Dept> findByPid(Long id) {
        return list("pid = ?1", id);
    }

    /**
     * 获取顶级部门
     * @return /
     */
    public List<Dept> findByPidIsNull() {
        return list("pid is null");
    }

    /**
     * 根据角色ID 查询
     * @param roleId 角色ID
     * @return /
     */
    @Query(value = "select d.* from sys_dept d, sys_roles_depts r where " +
            "d.dept_id = r.dept_id and r.role_id = ?1", nativeQuery = true)
    public Set<Dept> findByRoleId(Long roleId) {

    }

    /**
     * 判断是否存在子节点
     * @param pid /
     * @return /
     */
    public int countByPid(Long pid) {
        return find("SELECT COUNT(DISTINCT e.id) FROM Dept e where e.pid = ?1", pid).project(Integer.class).firstResult();
    }

    /**
     * 根据ID更新sub_count
     * @param count /
     * @param id /
     */
    @Transactional
//    @Query(value = " update sys_dept set sub_count = ?1 where dept_id = ?2 ",nativeQuery = true)
    public void updateSubCntById(Integer count, Long id) {
        update("set subCount=?1 where deptId=?1", count, id);
    }
}