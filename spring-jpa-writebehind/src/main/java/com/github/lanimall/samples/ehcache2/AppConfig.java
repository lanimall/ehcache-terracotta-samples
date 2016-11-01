package com.github.lanimall.samples.ehcache2;

import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ResourceLoader;

/**
 * Created by fabien.sanglier on 10/28/16.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "com.github.lanimall.samples.ehcache2.*" })
@PropertySource("classpath:/application.properties")
public class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    ResourceLoader resourceLoader;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    //this bean exposes a native Ehcache CacheManager object instead of the spring EhCacheManagerFactoryBean
    // that way you can autowire and access the cacheManager singleton throughout your app
    @Bean
    public FactoryBean<CacheManager> cacheManager() {
        String ehcacheConfigPath = System.getProperty("ehcache.config.path", "classpath:ehcache.xml");
        EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
        bean.setConfigLocation(resourceLoader.getResource(ehcacheConfigPath));
        bean.setCacheManagerName("cachetest");
        bean.setShared(true);
        return bean;
    }
}
