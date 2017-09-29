package com.jx.hunter.lvzhengpc.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jx.argo.interceptor.PostInterceptorAnnotation;
import com.jx.argo.interceptor.PreInterceptorAnnotation;
import com.jx.hunter.lvzhengpc.annotaion.impl.RequestMonitorImpl;

/**
 * 请求性能监控
 * @author duqingxiang
 *
 */

@PostInterceptorAnnotation(RequestMonitorImpl.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestMonitor {
	public int order() default 1;
}
