package top.hllcloud.platform.supports.websocket.entity.request;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import lombok.Data;
import top.hllcloud.platform.supports.websocket.util.GsonUtil;

/**
 * Websocket连接请求数据包装
 *
 * @author hllshiro
 * @date 2022/4/1 15:25
 */
@Data
public class RequestMessage {
    /**
     * 请求资源路径
     */
    private String uri;
    /**
     * 请求数据(反序列化到实体类使用)
     */
    private String data;
    /**
     * 自定义数据
     */
    private Dict extend;

    public RequestMessage() {
    }

    /**
     * @param json UTF8编码字节数组
     */
    public RequestMessage(byte[] json) {
        this(new String(json));
    }

    /**
     * @param json json字符串
     */
    public RequestMessage(String json) {
        RequestMessage convert = GsonUtil.fromJson(json, this.getClass());
        BeanUtil.copyProperties(convert, this);
    }
}
