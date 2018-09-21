package com.github.lanimall.ehcache;

import java.util.concurrent.TimeoutException;

/**
 * Created by fabien.sanglier on 9/20/18.
 */
public interface Counter {
    public long get();

    public long addAndGet(long delta) throws Exception;

    public long substractAndGet(long delta) throws Exception;
}
