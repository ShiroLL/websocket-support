package top.hllcloud.platform.supports.websocket.enums;

/**
 * @author hllshiro
 * @date 2022/4/13 14:21
 */
public enum CodecEnum {

    /**
     * Base32编码
     */
    BASE32("Base32", 0.75f),
    /**
     * Base32编码
     */
    BASE62("Base62", 0.75f),
    /**
     * Base64编码
     */
    BASE64("Base64", 0.75f),
    ;

    private final String name;
    private final float actualStorage;

    CodecEnum(String name, float actualStorage) {
        this.name = name;
        this.actualStorage = actualStorage;
    }

    /**
     * 通过编码名称获取编码枚举
     *
     * @param name
     * @return
     */
    public static CodecEnum fromName(String name) {
        for (CodecEnum value : CodecEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public float getActualStorage() {
        return actualStorage;
    }

    @Override
    public String toString() {
        return "CodecEnum{" +
                "name='" + name + '\'' +
                ", compressibility=" + actualStorage +
                '}';
    }
}
