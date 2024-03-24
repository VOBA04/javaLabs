package com.vova.laba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.vova.laba.utils.cache.GenericCache;
import com.vova.laba.utils.cache.SimpleCache;

@Configuration
public class CacheConfig<K, V> {

    @Bean
    @Scope("prototype")
    public GenericCache<K, V> cache() {
        return new SimpleCache<>();
    }
}
