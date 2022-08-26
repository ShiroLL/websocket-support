package top.hllcloud.platform.supports.websocket.entity.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import top.hllcloud.platform.supports.websocket.basic.ChannelStatus;

import java.io.IOException;

/**
 * Websocket Session包装类
 * 不提供set方法
 *
 * @author hllshiro
 */
public class WebsocketChannel<T extends ChannelStatus> {

    /**
     * 用于发送数据的session
     */
    private final WebSocketSession session;
    /**
     * 连接状态
     */
    private T status;

    public WebsocketChannel(WebSocketSession session) {
        this.session = session;
    }

    public WebsocketChannel(WebSocketSession session, T status) {
        this.session = session;
        this.status = status;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public ChannelStatus getStatus() {
        return status;
    }

    public void setStatus(T status) {
        this.status = status;
    }

    /**
     * 发送数据(可使用TextMessage或BinaryMessage)
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(WebSocketMessage<?> message) throws IOException {
        session.sendMessage(message);
    }

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    public void close() throws IOException {
        this.session.close();
    }

    /**
     * 关闭连接
     *
     * @param closeStatus 状态码
     * @throws IOException
     */
    public void close(CloseStatus closeStatus) throws IOException {
        this.session.close(closeStatus);
    }
}
