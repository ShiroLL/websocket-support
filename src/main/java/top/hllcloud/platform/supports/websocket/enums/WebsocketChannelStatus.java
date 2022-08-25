package top.hllcloud.platform.supports.websocket.enums;

import com.google.gson.JsonObject;

/**
 * Websocket连接状态枚举
 *
 * @author hllshiro
 */
public enum WebsocketChannelStatus {
    /*----------通用状态----------*/
    /**
     * 已连接、未认证
     */
    CONNECTED(1, "已连接"),
    /**
     * 已连接、已认证
     */
    AUTHORIZED(2, "已认证"),
    /**
     * 离线
     */
    OFFLINE(3, "离线");

    private final int value;
    private final String text;

    WebsocketChannelStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static WebsocketChannelStatus fromInt(int val) {
        for (WebsocketChannelStatus status : WebsocketChannelStatus.values()) {
            if (status.value == val) {
                return status;
            }
        }
        return null;
    }

    public static WebsocketChannelStatus fromStr(String val) {
        for (WebsocketChannelStatus status : WebsocketChannelStatus.values()) {
            if (status.text.equals(val)) {
                return status;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }

    public String text() {
        return text;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + this.name() +
                "\",\"value\":" + value +
                ",\"text\":\"" + text +
                "\"}";
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", this.name());
        json.addProperty("value", value);
        json.addProperty("text", text);
        return json;
    }
}
