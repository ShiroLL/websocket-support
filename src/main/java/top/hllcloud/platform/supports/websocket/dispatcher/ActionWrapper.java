package top.hllcloud.platform.supports.websocket.dispatcher;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Action执行器代理包装
 *
 * @author hllshiro
 * @date 2022/4/1 19:35
 */
@Getter
@AllArgsConstructor
public class ActionWrapper {
    /**
     * 代理类
     */
    private final Object clazz;
    /**
     * 原始类
     */
    private final Object targetClazz;
    /**
     * 代理方法
     */
    private final Method method;
    /**
     * 原始方法
     */
    private final Method targetMethod;
    /**
     * 过滤
     */
    private final String[] filter;

    /**
     * 代理调用
     *
     * @param args 方法参数列表
     * @return Object 返回值
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(this.clazz, args);
    }

    /**
     * 获取方法参数数量
     *
     * @return int
     */
    public int getParameterCount() {
        return this.method.getParameterCount();
    }

    /**
     * 获取方法参数列表
     *
     * @return Class<?>[]
     */
    public Class<?>[] getParameterTypes() {
        return this.method.getParameterTypes();
    }

    /**
     * 获取方法注解
     *
     * @return Annotation[]
     */
    public Annotation[] getAnnotations() {
        return this.method.getAnnotations();
    }

    /**
     * filter是否包含当前域
     * 如果filter为空，允许全部请求，否则仅允许包含请求
     *
     * @param endpoint
     * @return
     */
    public boolean isFilter(String endpoint) {
        return this.filter.length == 0 || ArrayUtil.contains(this.filter, endpoint);
    }
}
