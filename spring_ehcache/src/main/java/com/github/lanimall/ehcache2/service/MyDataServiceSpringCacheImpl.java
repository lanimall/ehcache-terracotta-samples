package com.github.lanimall.ehcache2.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Created by fabien.sanglier on 10/13/16.
 */

@Repository("MyDataServiceSpringCache")
public class MyDataServiceSpringCacheImpl extends MyDataServiceImpl implements MyDataService {

  @Override
  @Cacheable(value="stringCache", key="#uniqueId")
  public String getSomethingLongFromId(long uniqueId) {
    return slowQuery(uniqueId);
  }
}
