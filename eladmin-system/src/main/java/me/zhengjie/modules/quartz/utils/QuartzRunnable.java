package me.zhengjie.modules.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.utils.SpringBeanHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 执行定时任务
 * @author /
 */
@Slf4j
public class QuartzRunnable implements Callable<Object> {

    @Inject
    Object target;
    @Inject
    Method method;
    @Inject
    String params;

	QuartzRunnable(String beanName, String methodName, String params)
			throws NoSuchMethodException, SecurityException {
		this.target = SpringBeanHolder.getBean(beanName);
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
