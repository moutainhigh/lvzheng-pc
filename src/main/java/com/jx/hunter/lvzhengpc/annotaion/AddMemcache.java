/**
 * 
 */

package com.jx.hunter.lvzhengpc.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jx.argo.interceptor.PostInterceptorAnnotation;
import com.jx.argo.interceptor.PreInterceptorAnnotation;
import com.jx.hunter.lvzhengpc.annotaion.impl.AddMemcacheImpl;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2016年4月28日
 * @see
 * @since 1.0
 */
@PostInterceptorAnnotation(AddMemcacheImpl.class)
@PreInterceptorAnnotation(AddMemcacheImpl.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AddMemcache {

}
