package top.hllcloud.platform.supports.websocket.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.hllcloud.platform.supports.websocket.annotation.Action;
import top.hllcloud.platform.supports.websocket.annotation.ActionMapping;
import top.hllcloud.platform.supports.websocket.consts.SymbolConstant;
import top.hllcloud.platform.supports.websocket.dispatcher.ActionDispatcher;
import top.hllcloud.platform.supports.websocket.dispatcher.ActionWrapper;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 最后执行的监听器，输出参数列表
 *
 * @author hllshiro
 * @date 2022/4/1 11:25
 */
@Order
@Component
public class ActionCollectListener implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ActionDispatcher actionDispatcher;

    @Override
    public void run(String... args) {
        Map<String, ActionWrapper> cache = new HashMap<>();
        // <<<原始类才能扫描到方法注解，但通过代理类调用代理方法才能触发切面>>>
        // 扫描所有@Action注解类
        // 扫描所有@ActionMapping注解方法
        // 缓存
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        Map<String, Object> actionClass = applicationContext.getBeansWithAnnotation(Action.class);
        actionClass.forEach((clazzName, clazz) -> {
            try {
                Object proxyClazz = applicationContext.getBean(clazz.getClass());
                // 通过原始类获取方法注解
                Class<?> targetClass = AopUtils.getTargetClass(proxyClazz);
                Action action = targetClass.getAnnotation(Action.class);
                List<Method> methods = Arrays.stream(targetClass.getMethods())
                        .filter(method -> method.isAnnotationPresent(ActionMapping.class))
                        .collect(Collectors.toList());
                for (Method method : methods) {
                    ActionMapping actionMapping = method.getAnnotation(ActionMapping.class);
                    // 缓存代理类和代理方法
                    ActionWrapper proxyWrapper = new ActionWrapper(proxyClazz,
                            targetClass,
                            proxyClazz.getClass().getMethod(method.getName(), method.getParameterTypes()),
                            method,
                            action.filter());
                    // 处理url
                    cache.put(getUri(action, actionMapping), proxyWrapper);
                }
            } catch (Exception e) {
                log.error(" >>> 缓存Action失败", e);
                System.exit(0);
            }
        });
        actionDispatcher.putAll(cache);
        log.info(" >>> WebsocketEndpoint执行器缓存完成，执行器数量：{} ", cache.size());
    }

    /**
     * 拼接uri
     *
     * @param action
     * @param actionMapping
     * @return
     */
    private String getUri(Action action, ActionMapping actionMapping) {
        StringBuilder uri = new StringBuilder(actionMapping.value());
        if (!uri.toString().startsWith(SymbolConstant.LEFT_DIVIDE)) {
            uri.insert(0, SymbolConstant.LEFT_DIVIDE);
        }
        if (StrUtil.isNotBlank(action.value())) {
            uri.insert(0, action.value());
        }
        if (!uri.toString().startsWith(SymbolConstant.LEFT_DIVIDE)) {
            uri.insert(0, SymbolConstant.LEFT_DIVIDE);
        }
        return uri.toString();
    }
}
