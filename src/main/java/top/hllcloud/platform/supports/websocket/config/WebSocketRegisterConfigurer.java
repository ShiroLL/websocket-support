package top.hllcloud.platform.supports.websocket.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import top.hllcloud.platform.supports.websocket.annotation.WebSocketHandler;
import top.hllcloud.platform.supports.websocket.handler.BaseWebSocketHandler;

import javax.annotation.Resource;

/**
 * Websocket注册配置
 *
 * @author hllshiro
 */
@Configuration
@EnableWebSocket
public class WebSocketRegisterConfigurer implements WebSocketConfigurer {

    @Resource
    private ApplicationContext applicationContext;

    /**
     * spring websocket
     *
     * @param webSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        // 扫描被@WebsocketHandler修饰的类，注册WebsocketHandler
        applicationContext.getBeansWithAnnotation(WebSocketHandler.class)
                .forEach((clazzName, clazz) -> {
                    final WebSocketHandler webSocketHandler = clazz.getClass().getAnnotation(WebSocketHandler.class);
                    if (webSocketHandler.withSockJS()) {
                        webSocketHandlerRegistry.addHandler((BaseWebSocketHandler) clazz, webSocketHandler.value())
                                .addInterceptors(SpringUtil.getBean(webSocketHandler.interceptor()))
                                .setAllowedOriginPatterns("*")
                                .withSockJS();
                    } else {
                        webSocketHandlerRegistry
                                .addHandler((BaseWebSocketHandler) clazz, webSocketHandler.value())
                                .addInterceptors(SpringUtil.getBean(webSocketHandler.interceptor()))
                                .setAllowedOriginPatterns("*");
                    }
                });
    }


}
