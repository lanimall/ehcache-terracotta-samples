package com.github.lanimall.ehcache2.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by fabien.sanglier on 10/13/16.
 */

@Repository("MyDataServiceCacheAside")
public class MyDataServiceCacheAsideImpl extends MyDataServiceImpl implements MyDataService {

  @Value("#{cacheManager.getCache('stringCache')}")
  Cache someCache;

  @Override
  public String getSomethingLongFromId(long uniqueId) {
    String val;
    Element cacheElem;
    if(null == (cacheElem = someCache.get(uniqueId))){
      val = slowQuery(uniqueId);
      someCache.put(new Element(uniqueId, val));
    } else {
      val = (String)cacheElem.getObjectValue();
    }

    return val;
  }
}
