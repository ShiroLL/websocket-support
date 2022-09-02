package top.hllcloud.platform.supports.websocket.aop;


import cn.hutool.core.collection.CollUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.hllcloud.platform.supports.websocket.util.ValidUtil;

import javax.annotation.Resource;
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
 * token校验
 *
 * @author hllshiro
 */
@Aspect
@Component
public class ValidatedAop {

    @Resource
    private Validator validator;

    /**
     * 切入点
     */
    @Pointcut("@annotation(top.hllcloud.platform.supports.websocket.annotation.ActionMapping)")
    private void validateParam() {
    }

    @Before("validateParam()")
    public void before(JoinPoint joinPoint) throws ValidationException, NoSuchMethodException {
        ValidUtil.validateParameters(validator, joinPoint);
    }
}
