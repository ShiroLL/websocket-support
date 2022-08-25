package top.hllcloud.platform.supports.websocket.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记Websocket执行器类
 *
 * @author hllshiro
 * @date 2022/4/1 14:23
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    /**
     * uri
     */
    String value() default "";

    /**
     * 请求过滤，只有制定的uri允许使用此action
     */
    String[] filter() default {};

}
