package top.hllcloud.platform.supports.websocket.entity.response;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.TextMessage;
import top.hllcloud.platform.supports.websocket.util.GsonUtil;

import java.nio.charset.StandardCharsets;

/**
 * Websocket连接返回数据包装
 *
 * @author hllshiro
 * @date 2022/4/1 15:26
 */
@Data
@NoArgsConstructor
public class ResponseMessage {
    /**
     * 请求资源路径
     */
    private String uri;
    /**
     * 返回数据
     */
    private ResponseData data;
    /**
     * 自定义数据
     */
    private Dict extend;

    public ResponseMessage(String uri) {
        this.uri = uri;
    }

    public ResponseMessage(String uri, ResponseData data) {
        this.uri = uri;
        this.data = data;
    }

    /**
     * 转JSON字符串
     */
    public String toJson() {
        return GsonUtil.toJson(this);
    }

    /**
     * 转JSON字节数组
     */
    public byte[] toBytes() {
        return this.toJson().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 转TextMessage
     *
     * @return TextMessage
     */
    public TextMessage toMessage() {
        return new TextMessage(this.toBytes());
    }

}
