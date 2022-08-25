package top.hllcloud.platform.supports.websocket.dispatcher;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import top.hllcloud.platform.supports.websocket.entity.context.WebsocketSessionContext;
import top.hllcloud.platform.supports.websocket.entity.request.RequestMessage;
import top.hllcloud.platform.supports.websocket.entity.response.ResponseData;
import top.hllcloud.platform.supports.websocket.entity.response.ResponseMessage;
import top.hllcloud.platform.supports.websocket.util.ExceptionUtil;
import top.hllcloud.platform.supports.websocket.util.GsonUtil;
import top.hllcloud.platform.supports.websocket.util.WebSocketUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Action执行器代理缓存
 *
 * @author hllshiro
 * @date 2022/4/1 14:11
 */
@Component
@Slf4j
public class ActionDispatcher {

    /**
     * METHOD代理缓存
     * ConcurrentHashMap<url, Method>
     */
    private final Map<String, ActionWrapper> proxyActionCaches = new HashMap<>();

    /**
     * 构造方法
     *
     * @param applicationContext
     */
    public ActionDispatcher(ApplicationContext applicationContext) {
    }

    /**
     * 从WebSocketMessage转换到RequestMessage
     *
     * @param message
     * @return
     * @throws HttpMediaTypeNotSupportedException
     */
    private static RequestMessage messageToRequest(AbstractWebSocketMessage<?> message) throws HttpMediaTypeNotSupportedException {
        if (message instanceof TextMessage) {
            return new RequestMessage(((TextMessage) message).asBytes());
        } else if (message instanceof BinaryMessage) {
            return new RequestMessage(((BinaryMessage) message).getPayload().array());
        } else {
            throw new HttpMediaTypeNotSupportedException(message.getClass().getSimpleName());
        }
    }

    /**
     * 代理校验
     *
     * @param proxyWrapper
     * @param endpoint
     * @throws NoSuchMethodException
     */
    private static void checkFilter(ActionWrapper proxyWrapper, String endpoint) throws NoSuchMethodException {
        if (null == proxyWrapper) {
            throw new NoSuchMethodException();
        }
        // 请求域校验 Endpoint
        if (!proxyWrapper.isFilter(endpoint)) {
            throw new NoSuchMethodException();
        }
    }

    /**
     * 返回Map缓存
     */
    public Map<String, ActionWrapper> getMap() {
        return Collections.unmodifiableMap(this.proxyActionCaches);
    }

    public void putAll(Map<String, ActionWrapper> map) {
        this.proxyActionCaches.putAll(map);
    }

    /**
     * 调用请求url的执行器
     *
     * @param session WebSocketSession
     * @param message WebSocketMessage(TextMessage、BinaryMessage)
     * @throws IOException io异常
     */
    public void doAction(WebSocketSession session, AbstractWebSocketMessage<?> message) {
        RequestMessage request;
        ResponseMessage response = new ResponseMessage();
        try {
            // ThreadLocal缓存session
            WebsocketSessionContext.set(session);

            // message转request
            request = messageToRequest(message);

            // 从request中拷贝默认参数到response
            BeanUtil.copyProperties(request, response, "data");

            // 代理校验
            ActionWrapper proxyWrapper = this.proxyActionCaches.get(request.getUri());
            checkFilter(proxyWrapper, WebSocketUtil.getEndpoint(session));

            // 构造代理方法参数列表
            Object[] args = new Object[proxyWrapper.getParameterCount()];
            int index = 0;
            for (Class<?> type : proxyWrapper.getParameterTypes()) {
                if (type == WebSocketSession.class) {
                    args[index++] = session;
                } else if (type == RequestMessage.class) {
                    args[index++] = request;
                } else if (type == ResponseMessage.class) {
                    args[index++] = response;
                } else if (type == String.class) {
                    args[index++] = request.getData();
                } else {
                    if (null != request.getData()) {
                        args[index++] = GsonUtil.fromJson(request.getData(), type);
                    }
                }
            }

            // 包装返回值
            response.setData(ResponseData.success(proxyWrapper.invoke(args)));
        } catch (IllegalStateException e) {
            // 参数转换错误
            log.warn("请求参数错误", e);
            response.setData(ResponseData.error(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getCause().getMessage()));
        } catch (InvocationTargetException e) {
            // 异常代理包装（抛出的异常未被引用）
            // 获取原始Exception类
            Throwable runTime = e.getTargetException();
            if (ExceptionUtil.isBusinessException(runTime)) {
                // 业务异常
                response.setData(ResponseData.error(runTime.getMessage()));
            } else {
                // 其他异常
                log.error("业务异常", e);
                response.setData(ResponseData.error(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getCause().getMessage()));
            }
        } catch (IllegalAccessException | NoSuchMethodException | HttpMediaTypeNotSupportedException e) {
            // 请求接口错误
            // 请求域错误
            // 不受支持的数据格式
            response.setData(ResponseData.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()));
        } catch (Exception e) {
            log.error("业务异常", e);
            response.setData(ResponseData.error(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getCause().getMessage()));
        } finally {
            // 清理ThreadLocal缓存
            WebsocketSessionContext.clear();
        }

        try {
            // 判断是否需要发送数据
            if (!WebSocketUtil.hasFinish(session)) {
                session.sendMessage(response.toMessage());
            }
        } catch (IOException e) {
            log.warn("数据发送失败", e);
        }
    }

}
