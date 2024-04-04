package com.lbq.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * openfeign文件流传输
 *
 * @Author lbq
 * @Date 2024/4/5
 * @Version: 1.0
 */
@Configuration
public class OpenfeignFormConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }

    /**
     * 这里必须要设置 SpringEncoder 为默认的
     * 即 @Primary
     * 因为没有上边的 SpringFormEncoder 默认都是使用 SpringEncoder
     * 当你只引入上边的时候，我发现我别的 feign 调用就会出现错误，
     * 所以两者都引入，SpringEncoder 作为主的
     *
     * @param messageConverters
     * @return
     */
    @Bean
    @Primary
    public Encoder springEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringEncoder(messageConverters);
    }
}
