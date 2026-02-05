package com.velu.MovieBookingApplication.configuration;

import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
public class RedisConfiguration {

    @Bean
    public Config config(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return config;
    }

    @Bean
    public CacheManager cacheManager(Config config){
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("cache", RedissonConfiguration.fromConfig(config));
        manager.createCache("userList",)
    }

}
