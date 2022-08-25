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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * token校验
 *
 * @author hllshiro
 */
@Aspect
@Component
public class HibernateValidatedAop {

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
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            messages.add(constraintViolation.getMessage());
        }
        throw new ValidationException(messages.toString().substring(1, messages.toString().length() - 1));
    }
}
