package me.zhengjie.modules.quartz.utils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 执行定时任务
 * @author /
 */
@Slf4j
public class QuartzRunnable implements Callable<Object> {

    Object target;
    Method method;
    String params;

	QuartzRunnable(String beanName, String methodName, String params)
			throws NoSuchMethodException, SecurityException {
//fixme		this.target = SpringBeanHolder.getBean(beanName);
		this.params = params;
		if (StringUtils.isNotBlank(params)) {
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
		} else {
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	@SuppressWarnings({"unchecked","all"})
	public Object call() throws Exception {
		ReflectionUtils.makeAccessible(method);
		if (StringUtils.isNotBlank(params)) {
			method.invoke(target, params);
		} else {
			method.invoke(target);
		}
		return null;
	}
}
