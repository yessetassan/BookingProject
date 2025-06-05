package com.yesset.booking.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        log.info("Time to reset to Almaty by Yesset");

        return builder -> builder
                .timeZone(TimeZone.getTimeZone("GMT+5"));
    }

}
