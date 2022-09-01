package top.hllcloud.platform.supports.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.hllcloud.platform.supports.websocket.aop.ValidatedAop;

/**
 * 切面配置
 *
 * @author hllshiro
 */
@Configuration
public class BusinessAopConfig {

    @Bean
    public ValidatedAop validatedAop() {
        return new ValidatedAop();
    }
}
