package me.vt.aspect;

import cn.vt.util.JsonUtils;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import me.vt.annotation.Log;
import me.vt.domain.SysLog;
import me.vt.service.client.SysLogService;
import me.vt.utils.SecurityUtils;
import me.vt.utils.StringExUtils;

/**
 * @author Zheng Jie
 * @since 2018-11-24
 */
@Provider
@Slf4j
@Log
@Interceptor
public class LogAspect {
    private static final String UNKNOWN = "unknown";
    @Inject
    SysLogService sysLogService;
    @Inject
    SecurityUtils securityUtils;
    @Inject
    ResourceInfo resourceInfo; // 注入当前请求信息

    ThreadLocal<Long> currentTime = new ThreadLocal<>();
    @Context
    HttpHeaders headers;

    @AroundInvoke
    public Object logMethodCall(InvocationContext ctx) throws Exception {
        final Method method = ctx.getMethod();
        log.info("调用方法: {}", method.getName());
        currentTime.set(System.currentTimeMillis());
        Object result = ctx.proceed();
        SysLog sysLog = new SysLog("INFO", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        Object[] parameters = ctx.getParameters();
        sysLog.setParams(JsonUtils.toJsonString(parameters));
        String methodName = method.getDeclaringClass().getName() + "." + method.getName() + "()";
        sysLog.setMethod(methodName);
        me.vt.annotation.Log aopLog = method.getAnnotation(me.vt.annotation.Log.class);
        sysLog.setDescription(aopLog.value());
        sysLogService.save(getUsername(), StringExUtils.getBrowser(headers.getHeaderString("User-Agent")), getIp(),
                sysLog);
        log.info("方法执行完毕: {}", method.getName());
        return result;
    }

    /**
     * 获取ip地址
     */
    private String getIp() {
        String ip = headers.getHeaderString("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            // fixme:            ip = headers.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * 获取用户名
     * @return /
     */
    public String getUsername() {
        try {
            return securityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
