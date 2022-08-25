package top.hllcloud.platform.supports.websocket.enums;

import org.springframework.web.socket.CloseStatus;

/**
 * 战术项目自定义Websocket关闭状态
 * code值范围 (4500,5000)
 *
 * @author hllshiro
 * @date 2022/4/4 14:11
 */
public enum CloseStatusEnum {
    /**
     * 非法连接（用户UA标识解析失败，或设备不在支持列表）
     */
    ILLEGALITY_CONNECTION(4501, "非法连接"),
    /**
     * 账户已在其他设备登录
     */
    REPEAT_LOGIN(4502, "账户已在其他设备登录"),
    /**
     * 登陆已过期
     */
    OVER_EXPIRES(4503, "登陆已过期"),
    ;

    int code;
    String reason;

    CloseStatusEnum(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public CloseStatus getCloseStatus() {
        return new CloseStatus(this.code, this.reason);
    }
}
