package com.abiao.sina.common.utils;

import com.abiao.web.infrastructure.common.IdGenerator;
import com.abiao.web.infrastructure.common.UUIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeanConfiguration {

    @Bean
    public IdGenerator initIdGenerator() {
        return new UUIDGenerator();
    }
}
