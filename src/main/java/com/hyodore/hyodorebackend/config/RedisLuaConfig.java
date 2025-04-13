package com.hyodore.hyodorebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisLuaConfig {

    @Bean
    public RedisScript<String> safePopScript() {
        return RedisScript.of(
                new ClassPathResource("lua/safe-pop.lua"),
                String.class
        );
    }
}
