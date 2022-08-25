package top.hllcloud.platform.supports.websocket.util;

import cn.hutool.core.net.NetUtil;
import org.springframework.web.socket.WebSocketSession;
import top.hllcloud.platform.supports.websocket.consts.WebSocketConstant;

import java.net.InetSocketAddress;

/**
 * WebSocket Session 工具
 *
 * @author hllshiro
 * @date 2022/4/5 9:42
 */
public class WebSocketUtil {

    /**
     * 获取属性
     *
     * @param session
     * @param key
     * @return
     */
    public static Object get(WebSocketSession session, String key) {
        return session.getAttributes().get(key);
    }

    /**
     * 获取属性
     *
     * @param session
     * @param key
     * @param def
     * @return
     */
    public static Object getOrDefault(WebSocketSession session, String key, Object def) {
        return session.getAttributes().getOrDefault(key, def);
    }

    /**
     * 设置属性
     *
     * @param session
     * @param key
     * @param val
     */
    public static void put(WebSocketSession session, String key, Object val) {
        session.getAttributes().put(key, val);
    }

    /**
     * 移除属性
     *
     * @param session
     * @param key
     * @return
     */
    public static Object remove(WebSocketSession session, String key) {
        return session.getAttributes().remove(key);
    }

    /**
     * 获取token
     *
     * @param session
     * @return
     */
    public static String getToken(WebSocketSession session) {
        return (String) WebSocketUtil.get(session, WebSocketConstant.TOKEN_NAME);
    }

    /**
     * 设置token
     *
     * @param session
     * @param token
     */
    public static void putToken(WebSocketSession session, String token) {
        WebSocketUtil.put(session, WebSocketConstant.TOKEN_NAME, token);
    }

    /**
     * 获取endpoint
     *
     * @param session
     * @return
     */
    public static String getEndpoint(WebSocketSession session) {
        return (String) WebSocketUtil.get(session, WebSocketConstant.ENDPOINT);
    }

    /**
     * 设置session缓冲区大小
     *
     * @param session
     * @param limit
     */
    public static void setMessageSizeLimit(WebSocketSession session, int limit) {
        session.setTextMessageSizeLimit(limit);
        session.setBinaryMessageSizeLimit(limit);
    }

    /**
     * 标记请求事务提前结束，这将导致Action执行后不自动调用session.sendMessage方法
     *
     * @param session
     */
    public static void finish(WebSocketSession session) {
        WebSocketUtil.put(session, WebSocketConstant.REQUEST_SESSION_FINISH, true);
    }

    /**
     * 请求事务是否已经结束
     * 每次获取后即清除该属性
     *
     * @param session
     * @return
     */
    public static boolean hasFinish(WebSocketSession session) {
        boolean hasFinish = (boolean) WebSocketUtil.getOrDefault(session, WebSocketConstant.REQUEST_SESSION_FINISH, false);
        WebSocketUtil.remove(session, WebSocketConstant.REQUEST_SESSION_FINISH);
        return hasFinish;
    }

    /**
     * 获取mac地址
     * FIXME 代理后可能失效
     *
     * @param session
     * @return
     */
    public static String getMacAddress(WebSocketSession session) {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        if (null == remoteAddress) {
            return null;
        }
        return NetUtil.getMacAddress(remoteAddress.getAddress());
    }

    /**
     * 获取IPV4地址
     * FIXME 代理后可能失效
     *
     * @param session
     * @return
     */
    public static String getIp(WebSocketSession session) {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        if (null == remoteAddress) {
            return null;
        }
        return NetUtil.getIpByHost(remoteAddress.getAddress().getHostAddress());
    }

    /**
     * 获取端口号
     * FIXME 代理后可能失效
     *
     * @param session
     * @return
     */
    public static String getPort(WebSocketSession session) {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        if (null == remoteAddress) {
            return null;
        }
        return String.valueOf(remoteAddress.getPort());
    }
}
