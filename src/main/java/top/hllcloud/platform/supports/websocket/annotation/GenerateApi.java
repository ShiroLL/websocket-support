package top.hllcloud.platform.supports.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 辅助生成Cpp代码注解
 * 参数为空时使用IStarBaseSerial，否则添加前缀CStar
 *
 * @author hllshiro
 * @date 2022/4/14 9:20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateApi {
    /**
     * 接口名称
     */
    String value();

    /**
     * 返回参数是否为Array
     */
    boolean isArray() default false;

    /**
     * 请求参数名称
     */
    Class<?> request() default Object.class;

    /**
     * 返回参数名称
     */
    Class<?> response() default Object.class;

    /**
     * 泛型类型
     */
    Class<?> T() default Object.class;
}
