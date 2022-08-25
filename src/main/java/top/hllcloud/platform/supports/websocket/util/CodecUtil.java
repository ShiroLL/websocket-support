package top.hllcloud.platform.supports.websocket.util;

import cn.hutool.core.codec.Base32;
import cn.hutool.core.codec.Base62;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import top.hllcloud.platform.supports.websocket.enums.CodecEnum;

/**
 * 编解码工具
 *
 * @author hllshiro
 * @date 2022/4/13 14:17
 */
public class CodecUtil {
    /**
     * encode
     *
     * @param source 字节数组
     * @param len    实际长度
     * @param codec  编码方式
     * @return
     */
    public static String encode(byte[] source, int len, CodecEnum codec) {
        switch (codec) {
            case BASE32:
                if (len < source.length) {
                    // 当前分片较小
                    byte[] shrink = new byte[len];
                    ArrayUtil.copy(source, shrink, len);
                    return Base32.encode(shrink);
                } else {
                    return Base32.encode(source);
                }
            case BASE62:
                if (len < source.length) {
                    // 当前分片较小
                    byte[] shrink = new byte[len];
                    ArrayUtil.copy(source, shrink, len);
                    return Base62.encode(shrink);
                } else {
                    return Base62.encode(source);
                }
            case BASE64:
                if (len < source.length) {
                    // 当前分片较小
                    byte[] shrink = new byte[len];
                    ArrayUtil.copy(source, shrink, len);
                    return Base64.encode(shrink);
                } else {
                    return Base64.encode(source);
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * decode
     *
     * @param source 字节数组
     * @param codec  编码方式
     * @return
     */
    public static byte[] decode(String source, CodecEnum codec) {
        switch (codec) {
            case BASE32:
                return Base32.decode(source);
            case BASE62:
                return Base62.decode(source);
            case BASE64:
                return Base64.decode(source);
            default:
                throw new IllegalArgumentException();
        }
    }
}
