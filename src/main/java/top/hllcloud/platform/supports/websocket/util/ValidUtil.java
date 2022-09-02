package top.hllcloud.platform.supports.websocket.util;

import cn.hutool.core.collection.CollUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hllshiro
 * @date 2022/9/2 8:54
 */
public class ValidUtil {

    public static void validateParameters(Validator validator, JoinPoint joinPoint) throws NoSuchMethodException {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // groups分组校验分组，添加默认Default分组避免未使用groups的属性被忽略
        final ArrayList<Class<?>> groups = CollUtil.newArrayList(Default.class);

        // 过滤方法参数中的group
        final Annotation[][] parameterAnnotations = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes()).getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                try {
                    if (annotation.annotationType().getName().startsWith("javax.validation.constraints")) {
                        Class<?>[] classes = (Class<?>[]) annotation.getClass().getMethod("groups").invoke(annotation);
                        groups.addAll(Arrays.asList(classes));
                    }
                } catch (IllegalAccessException | InvocationTargetException ignore) {
                }
            }
        }

        // 调用Validator解析器校验
        Set<ConstraintViolation<Object>> constraintViolations =
                validator.forExecutables()
                        .validateParameters(joinPoint.getThis(),
                                method,
                                joinPoint.getArgs(),
                                groups.toArray(new Class<?>[]{}));
        if (constraintViolations.size() == 0) {
            return;
        }
        // 抛出错误信息
        String err = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).toString();
        throw new ValidationException(err.substring(1, err.length() - 1));
    }
}
