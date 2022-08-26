package top.hllcloud.platform.supports.websocket.util;

import cn.hutool.core.util.StrUtil;
import com.google.gson.*;
import lombok.SneakyThrows;
import org.springframework.web.socket.WebSocketSession;
import top.hllcloud.platform.supports.websocket.basic.ChannelStatus;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author hllshiro
 * @date 2022/4/5 16:13
 */
public class GsonUtil {

    private static final Gson GSON = new GsonBuilder()
            // 注册自定义序列化
            .registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapter(String.class, new StringAdapter())
            .registerTypeAdapter(Long.class, new NumberAdapter())
            .registerTypeAdapter(ChannelStatus.class, new ChannelStatusAdapter())
            // 静态属性过滤
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.FINAL)
            // 属性过滤
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    if (aClass == WebSocketSession.class) {
                        return true;
                    }
                    return false;
                }
            })
            .create();

    /**
     * json字符串转class
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * class转json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * class转JsonObject
     *
     * @param obj
     * @return
     */
    public static JsonElement toJsonTree(Object obj) {
        return GSON.toJsonTree(obj);
    }

    /**
     * 自定义date类型转换
     * C++类Date类型使用int64，JavaBean的Date类型需要序列化为long类型
     */
    private static class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            if (null == date) {
                return null;
            }
            return new JsonPrimitive(String.valueOf(date.getTime()));
        }

        @Override
        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement instanceof JsonPrimitive) {
                String timeStr = jsonElement.getAsString();
                return StrUtil.isBlank(timeStr) ? null : new Date(Long.parseLong(timeStr));
            }
            return null;
        }
    }

    /**
     * String类型反序列化特殊处理
     */
    private static class StringAdapter implements JsonDeserializer<String> {

        @Override
        public String deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            // 目标类型是String且json为array或object
            if (String.class == type && (jsonElement instanceof JsonObject || jsonElement instanceof JsonArray)) {
                return jsonElement.toString();
            }
            // 目标类型是String且json为value
            if (String.class == type && jsonElement instanceof JsonPrimitive) {
                return jsonElement.getAsString();
            }
            // 其他类型执行默认实现
            return jsonDeserializationContext.deserialize(jsonElement, type);
        }
    }

    /**
     * Long类型序列化特殊处理
     */
    private static class NumberAdapter implements JsonSerializer<Number> {

        @Override
        public JsonElement serialize(Number number, Type type, JsonSerializationContext jsonSerializationContext) {
            if (number.longValue() > Integer.MAX_VALUE) {
                return new JsonPrimitive(number.toString());
            }
            return new JsonPrimitive(number);
        }
    }

    /**
     * 自定义byte[]类型转换
     * 用于存储文件字节数据
     */
    private static class ByteArrayAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

        @Override
        public JsonElement serialize(byte[] bytes, Type type, JsonSerializationContext jsonSerializationContext) {
            return null;
        }

        @Override
        public byte[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new byte[0];
        }
    }

    /**
     * 自定义ChannelStatus枚举转换
     */
    private static class ChannelStatusAdapter
            implements JsonSerializer<ChannelStatus>,
            JsonDeserializer<ChannelStatus> {

        @SneakyThrows
        @Override
        public ChannelStatus deserialize(
                JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            if (((JsonPrimitive) jsonElement).isNumber()) {
                return ChannelStatus.fromInt(jsonElement.getAsInt());
            }
            return ChannelStatus.fromStr(jsonElement.getAsString());
        }

        @Override
        public JsonElement serialize(
                ChannelStatus channelStatus, Type type,
                JsonSerializationContext jsonSerializationContext) {
            return channelStatus.toJson();
        }
    }
}
