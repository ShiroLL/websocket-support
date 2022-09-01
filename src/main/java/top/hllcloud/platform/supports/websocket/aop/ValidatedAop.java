package top.hllcloud.platform.supports.websocket.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
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
    @Pointcut("@annotation(org.springframework.validation.annotation.Validated)")
    private void validateParam() {
    }

    @Before("validateParam()")
    public void before(JoinPoint joinPoint) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations =
                validator.forExecutables()
                        .validateParameters(joinPoint.getThis(),
                                ((MethodSignature) joinPoint.getSignature()).getMethod(),
                                joinPoint.getArgs());
        if (constraintViolations.size() == 0) {
            return;
        }
        String err = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).toString();
        throw new ValidationException(err.substring(1, err.length() - 1));
    }
}
