package com.colak.springtutorial.config;

import com.colak.springtutorial.employee.dto.EmployeeDTO;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {

        // See https://medium.com/@prasanta-paul/redis-gson-serializer-3634d00d02b4
        // Redis Cache by default uses Jackson based JSON Serialization (GenericJackson2JsonRedisSerializer) which embeds Class name mapping in serialized JSON data.
        // Embedded Class name guarantees Type safety (and avoid Class Cast Exceptions) during de-serialization process.
        // But may be redundant for scenarios where Application knows its POJO or Model structure
        return builder -> builder
                .withCacheConfiguration("employees",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5)) // TTL 5 minutes
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisGsonSerializer<>(EmployeeDTO.class))));
    }
}

