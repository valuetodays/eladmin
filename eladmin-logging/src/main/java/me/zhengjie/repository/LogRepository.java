package me.zhengjie.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import me.zhengjie.MyPanacheRepository;
import me.zhengjie.domain.SysLog;

/**
 * @author Zheng Jie
 * @since 2018-11-24
 */
@ApplicationScoped
public class LogRepository extends MyPanacheRepository<SysLog> {

    /**
     * 根据日志类型删除信息
     * @param logType 日志类型
     */
    @Transactional
//    @Query(value = "delete from sys_log where log_type = ?1", nativeQuery = true)
    public void deleteByLogType(String logType) {
        delete("where logType=?1", logType);
    }
}
