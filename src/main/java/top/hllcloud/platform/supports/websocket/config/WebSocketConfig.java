package top.hllcloud.platform.supports.websocket.config;


import cn.hutool.core.io.unit.DataSizeUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.hllcloud.platform.supports.websocket.enums.CodecEnum;

/**
 * Websocket配置
 *
 * @author hllshiro
 * @date 2022/4/13 8:41
 */
@Data
@Component
@ConfigurationProperties("spring.websocket")
public class WebSocketConfig {

    /**
     * 缓冲区大小
     */
    private String bufferSize = "8KB";

    /**
     * 编码方式
     */
    private CodecEnum codec = CodecEnum.BASE64;

    public CodecEnum getCodec() {
        return this.codec;
    }

    /**
     * 将自动注入的String转为枚举
     *
     * @param codec
     */
    public void setCodec(String codec) {
        this.codec = CodecEnum.fromName(codec);
    }

    /**
     * 返回bufferSize对应的字节数，预留额外的8192字节给文件编码外的其他数据
     *
     * @return int
     */
    public int getBufferSizeByte() {
        return Math.toIntExact(DataSizeUtil.parse(this.bufferSize)) + 8192;
    }

    /**
     * 返回根据编码压缩率计算buffSize实际能够存储的大小，向下取整保证不会越界
     *
     * @return int
     */
    public int getActualStorageSize() {
        return (int) Math.floor(this.getBufferSizeByte() * this.codec.getActualStorage());
    }
}
