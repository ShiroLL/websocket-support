package top.hllcloud.platform.supports.websocket.annotation;

import java.lang.annotation.*;

/**
 * 用于鉴别观察者类别，需要指明被观察者的类名
 *
 * @author hllshiro
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObserverRegister {
    Class value();
}
