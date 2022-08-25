package top.hllcloud.platform.supports.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记Websocket执行器方法
 *
 * @author hllshiro
 * @date 2022/4/1 14:22
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMapping {
    /**
     * uri路径映射
     *
     * @return
     */
    String value();

    /**
     * token校验
     */
    boolean check() default true;
}
