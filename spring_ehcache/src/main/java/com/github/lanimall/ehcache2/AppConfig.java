/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.lanimall.ehcache2;

import com.github.lanimall.ehcache2.service.MyDataService;
import com.github.lanimall.ehcache2.service.MyDataServiceCacheAsideImpl;
import com.github.lanimall.ehcache2.service.MyDataServiceImpl;
import com.github.lanimall.ehcache2.service.MyDataServiceSpringCacheImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableCaching
@ComponentScan({ "com.github.lanimall.ehcache2.*" })
@PropertySource("classpath:/application.properties")
public class AppConfig {
  private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

  @Autowired
  ResourceLoader resourceLoader;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  //begin: these 2 beans are required to use the built-in spring @cacheable constructs
  @Bean
  public CacheManager getEhCacheManager(){
    return new EhCacheCacheManager(getEhCacheManagerFactory().getObject());
  }

  @Bean
  public EhCacheManagerFactoryBean getEhCacheManagerFactory() {
    String ehcacheConfigPath = System.getProperty("ehcache.config.path", "classpath:ehcache.xml");
    EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
    bean.setConfigLocation(resourceLoader.getResource(ehcacheConfigPath));
    bean.setCacheManagerName("cachetest-springcacheable");
    bean.setShared(true);
    return bean;
  }
  // End of: these 2 beans are required to use the built-in spring @cacheable constructs

  //this bean exposes a native Ehcache CacheManager object instead of the spring EhCacheManagerFactoryBean
  // that way you can autowire and access the cacheManager singleton throughout your app
  @Bean
  public FactoryBean<net.sf.ehcache.CacheManager> cacheManager() {
    EhCacheManagerFactoryBean bean = getEhCacheManagerFactory();
    bean.setCacheManagerName("cachetest-nativeehcache");
    return bean;
  }

  @Autowired
  @Qualifier("MyDataServiceCacheAside")
  MyDataService myDataServiceCacheAside;

  @Autowired
  @Qualifier("MyDataServiceNoCache")
  MyDataService myDataService;

  @Autowired
  @Qualifier("MyDataServiceSpringCache")
  MyDataService myDataServiceSpringCache;

  @Bean(name = "DataServiceTester")
  public MyDataServiceTester getDataServiceTester(){
    return new MyDataServiceTester(myDataService);
  }

  @Bean(name = "DataServiceWithCacheAsideTester")
  public MyDataServiceTester getDataServiceWithCacheAsideTester(){
    return new MyDataServiceTester(myDataServiceCacheAside);
  }

  @Bean(name = "DataServiceWithSpringCache")
  public MyDataServiceTester getDataServiceWithSpringCache(){
    return new MyDataServiceTester(myDataServiceSpringCache);
  }
}