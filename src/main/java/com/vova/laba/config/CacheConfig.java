package com.vova.laba.config;

import com.vova.laba.utils.cache.GenericCache;
import com.vova.laba.utils.cache.SimpleCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CacheConfig<K, V> {

  @Bean
  @Scope("prototype")
  public GenericCache<K, V> cache() {
    return new SimpleCache<>();
  }
}
