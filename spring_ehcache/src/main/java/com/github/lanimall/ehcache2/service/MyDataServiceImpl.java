package com.github.lanimall.ehcache2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.Random;

/**
 * Created by fabien.sanglier on 10/13/16.
 */

@Repository("MyDataServiceNoCache")
public class MyDataServiceImpl implements MyDataService {

  @Value("${com.github.lanimall.ehcache2.longrunning.sleeptimemillis:200}")
  int sleepTime;

  @Override
  public String getSomethingLongFromId(long uniqueId) {
    return slowQuery(uniqueId);
  }

  protected String slowQuery(long uniqueId){
    try {
      return String.format("Value-%d-%d", uniqueId, new Random(System.currentTimeMillis()).nextLong());
    } finally {
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
