package top.hllcloud.platform.supports.websocket.util;

import cn.hutool.core.util.ArrayUtil;

/**
 * 异常处理工具类
 *
 * @author hllshiro
 * @date 2022/4/12 11:47
 */
public class ExceptionUtil {

    /**
     * 自定义业务异常列表
     */
    private final static String[] BUSINESS_EXCEPTION_LIST = {
            "AuthException",
            "LibreOfficeException",
            "PermissionException",
            "RequestMethodException",
            "ServiceException",
            "ValidationException"
    };

    /**
     * 判断是否为业务异常
     *
     * @param t
     * @return
     */
    public static boolean isBusinessException(Throwable t) {
        if (!(t instanceof RuntimeException)) {
            return false;
        }
        String simpleName = t.getClass().getSimpleName();
        String superName = t.getClass().getSuperclass().getSimpleName();

        return ArrayUtil.containsAny(BUSINESS_EXCEPTION_LIST, simpleName, superName);
    }
}
