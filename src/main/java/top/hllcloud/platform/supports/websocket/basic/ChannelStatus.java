package top.hllcloud.platform.supports.websocket.basic;

import com.google.gson.JsonElement;

/**
 * 枚举接口
 *
 * @author hllshiro
 * @date 2022/8/26 14:57
 */
public interface ChannelStatus {
    /**
     * int转枚举，子类必须实现
     * @param aInt
     * @return
     * @throws NoSuchMethodException
     */
    static ChannelStatus fromInt(int aInt) throws NoSuchMethodException {
        throw new NoSuchMethodException("non realization");
    }

    /**
     * str转枚举，子类必须实现
     * @param aString
     * @return
     * @throws NoSuchMethodException
     */
    static ChannelStatus fromStr(String aString) throws NoSuchMethodException {
        throw new NoSuchMethodException("non realization");
    }

    JsonElement toJson();
}
