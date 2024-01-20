package com.lbq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security 配置
 *
 * @Author lbq
 * @Date 2024/1/13
 * @Version: 1.0
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .antMatchers("/*").permitAll();
        httpSecurity.csrf().disable();
        return httpSecurity.build();
    }
}
