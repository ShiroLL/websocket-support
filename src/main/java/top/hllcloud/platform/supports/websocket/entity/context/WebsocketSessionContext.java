package top.hllcloud.platform.supports.websocket.entity.context;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebsocketSession上下文缓存
 *
 * @author hllshiro
 * @date 2022/4/4 16:43
 */
public class WebsocketSessionContext {

    private static final ThreadLocal<WebSocketSession> SESSION_HOLDER = new ThreadLocal<>();

    public static void set(WebSocketSession webSocketSession) {
        SESSION_HOLDER.set(webSocketSession);
    }

    public static WebSocketSession get() {
        return SESSION_HOLDER.get();
    }

    public static void clear() {
        SESSION_HOLDER.remove();
    }

}
