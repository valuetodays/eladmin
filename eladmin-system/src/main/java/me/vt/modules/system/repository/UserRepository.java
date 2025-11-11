package me.vt.modules.system.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Set;
import me.vt.common.repository.MyPanacheRepository;
import me.vt.modules.system.domain.User;

/**
 * @author Zheng Jie
 * @since 2018-11-22
 */
@ApplicationScoped
public class UserRepository extends MyPanacheRepository<User> {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return /
     */
    public User findByUsername(String username) {
        return find("username = ?1", username).firstResult();
    }

    /**
     * 根据邮箱查询
     * @param email 邮箱
     * @return /
     */
    public User findByEmail(String email) {
        return find("email = ?1", email).firstResult();
    }

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return /
     */
    public User findByPhone(String phone) {
        return find("phone = ?1", phone).firstResult();
    }

    /**
     * 修改密码
     * @param username 用户名
     * @param pass 密码
     * @param lastPasswordResetTime /
     */
    @Transactional
    //    @Query(value = "update sys_user set password = ?2 , pwd_reset_time = ?3 where username = ?1",nativeQuery = true)
    public void updatePass(String username, String pass, Date lastPasswordResetTime) {
        update("set password = ?2 , pwdResetTime = ?3  where username = ?1", username, pass, lastPasswordResetTime);
    }

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    @Transactional
    //    @Query(value = "update sys_user set email = ?2 where username = ?1",nativeQuery = true)
    public void updateEmail(String username, String email) {
        update("set email = ?2 where username = ?1", username, email);
    }

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    //    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id IN ?1", nativeQuery = true)
    public int countByDepts(Set<Long> deptIds) {
        return (int) find("deptId in ?1", deptIds).count();
    }

    /**
     * 重置密码
     * @param ids 、
     * @param pwd 、
     */
    @Transactional
    //    @Query(value = "update sys_user set password = ?2 where user_id in ?1",nativeQuery = true)
    public void resetPwd(Set<Long> ids, String pwd) {
        update("set password = ?2 where id in ?1", pwd, ids);
    }
}
