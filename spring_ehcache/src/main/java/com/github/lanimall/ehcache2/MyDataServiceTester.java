package com.github.lanimall.ehcache2;

import com.github.lanimall.ehcache2.service.MyDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by fabien.sanglier on 10/13/16.
 */
public class MyDataServiceTester {
  private static final Logger log = LoggerFactory.getLogger(MyDataServiceTester.class);

  @Value("${com.github.lanimall.ehcache2.iterations:2}")
  int iterations;

  @Value("${com.github.lanimall.ehcache2.opsperiteration:100}")
  int opsPerIteration;

  private MyDataService myDataService;

  public MyDataServiceTester(MyDataService myDataService) {
    this.myDataService = myDataService;
  }

  public void test(){
    long start = System.currentTimeMillis();
    for(int itcounter = 0 ; itcounter < iterations; itcounter++) {
      for (int i = 0; i < opsPerIteration; i++) {
        log.info("Result : {}", myDataService.getSomethingLongFromId(i));
      }
    }
    log.info("{} Operations - Processed in millis : {}", iterations * opsPerIteration, System.currentTimeMillis() - start);
  }
}
