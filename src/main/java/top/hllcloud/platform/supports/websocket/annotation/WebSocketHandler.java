package top.hllcloud.platform.supports.websocket.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;
import top.hllcloud.platform.supports.websocket.config.DefaultHandshakeInterceptor;

import java.lang.annotation.*;

/**
 * 标记websocketHandler类别
 *
 * @author hllshiro
 * @date 2022/4/1 11:13
 */
@Component
@Scope("singleton")
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketHandler {
    /**
     * Endpoint（URI）
     *
     * @return
     */
    String value();

    /**
     * withSockJS
     */
    boolean withSockJS() default false;

    /**
     * 握手拦截器
     */
    Class<? extends HandshakeInterceptor> interceptor() default DefaultHandshakeInterceptor.class;
}
