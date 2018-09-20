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
package sample.rs.service.hello2;

import io.swagger.annotations.Api;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sample.rs.service.HelloService;

import javax.ws.rs.Path;

@Path("/sayHelloWithCache2")
@Api("/sayHelloWithCache2")
public class HelloServiceWithCacheImpl2 implements HelloService {
    private static final String hello = "hello2";
    private Cache stringCache;

    public HelloServiceWithCacheImpl2(Cache stringCache) {
        this.stringCache = stringCache;
    }

    public String sayHello(String a) {
        String val;
        Element cacheElem;
        boolean isCacheHit = false;
        long timeNow = System.currentTimeMillis();
        if (null == (cacheElem = stringCache.get(a))) {
            isCacheHit = false;
            val = String.format("%s-%s (%s)", (null == a)?"null":a, timeNow, "Hello2");
            stringCache.put(new Element(a, val));
        } else {
            isCacheHit = true;
            val = (String) cacheElem.getObjectValue();
        }

        return String.format("%d - %s %s (From Cache? %s), Welcome to CXF RS Spring Boot World!!!", timeNow, hello, val, isCacheHit);
    }
}
