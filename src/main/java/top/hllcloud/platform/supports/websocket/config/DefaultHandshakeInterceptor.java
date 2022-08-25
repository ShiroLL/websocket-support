package top.hllcloud.platform.supports.websocket.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 请求握手拦截器默认是实现
 *
 * @author hllshiro
 */
@Component
public class DefaultHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * 连接建立前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest t, ServerHttpResponse e, WebSocketHandler r, Map<String, Object> p) {
        return true;
    }

    /**
     * 连接建立后
     */
    @Override
    public void afterHandshake(ServerHttpRequest t, ServerHttpResponse e, WebSocketHandler r, Exception n) {
    }
}
