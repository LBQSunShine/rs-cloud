package com.lbq.config;

import com.lbq.constants.TokenConstants;
import com.lbq.service.RedisService;
import com.lbq.utils.UUIDUtils;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决HttpMessageConverters Bean未注册问题
 *
 * @Author lbq
 * @Date 2024/1/21
 * @Version: 1.0
 */
@Configuration
public class OpenfeignConfig implements RequestInterceptor {

    @Autowired
    private RedisService redisService;

    @Bean
    Logger.Level feignLevel() {
        //这里记录所有
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new PhpMappingJackson2HttpMessageConverter());
        return new ObjectFactory<HttpMessageConverters>() {
            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return httpMessageConverters;
            }
        };
    }

    public class PhpMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        PhpMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
            setSupportedMediaTypes(mediaTypes);
        }
    }

    /**
     * openfeign调用自动添加请求头
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String uuid = UUIDUtils.getUUID();
        redisService.set(uuid, uuid, 5 * 60L);
        requestTemplate.header(TokenConstants.FROM, uuid);
    }
}
