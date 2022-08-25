package top.hllcloud.platform.supports.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import top.hllcloud.platform.supports.websocket.annotation.WebSocketHandler;
import top.hllcloud.platform.supports.websocket.config.WebSocketConfig;
import top.hllcloud.platform.supports.websocket.util.WebSocketUtil;

import javax.annotation.Resource;

/**
 * @author hllshiro
 * @date 2022/4/19 11:50
 */
public abstract class BaseWebSocketHandler extends AbstractWebSocketHandler {
    public static final String CONNECT_PREFIX = "[{}:{}][{}] - ";
    public static final String CONNECTED = "建立连接";
    public static final String DISCONNECTED = "断开连接";
    public static final String ERROR = "发生错误";
    public static final String MESSAGE = "收到消息，长度:{}";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Lazy
    @Resource
    protected WebSocketConfig webSocketConfig;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.debug(CONNECT_PREFIX + CONNECTED,
                WebSocketUtil.getIp(session),
                WebSocketUtil.getPort(session),
                this.getEndpoint());
        // 设置缓冲大小
        session.setBinaryMessageSizeLimit(webSocketConfig.getBufferSizeByte());
        session.setTextMessageSizeLimit(webSocketConfig.getBufferSizeByte());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        log.debug(CONNECT_PREFIX + MESSAGE,
                WebSocketUtil.getIp(session),
                WebSocketUtil.getPort(session),
                this.getEndpoint(),
                message.getPayloadLength());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        /*if (EOFException.class == exception.getClass() && null == exception.getCause()) {
            return;
        }*/
        log.error(CONNECT_PREFIX + ERROR,
                WebSocketUtil.getIp(session),
                WebSocketUtil.getPort(session),
                this.getEndpoint());
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.debug(CONNECT_PREFIX + DISCONNECTED,
                WebSocketUtil.getIp(session),
                WebSocketUtil.getPort(session),
                this.getEndpoint());
    }

    /**
     * 是否支持部分消息传输，默认为false
     * 若设置为true，则一个消息会被拆分成多个消息，多次调用handleMessage，可通过WebSocketMessage的isLast方法判断是否为最后一次消息
     */
    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    protected String getEndpoint() {
        WebSocketHandler annotation = this.getClass().getAnnotation(WebSocketHandler.class);
        if (null == annotation) {
            return null;
        }
        return annotation.value();
    }

}
