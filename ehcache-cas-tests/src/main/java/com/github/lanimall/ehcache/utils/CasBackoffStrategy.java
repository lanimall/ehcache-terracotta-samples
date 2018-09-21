package com.github.lanimall.ehcache.utils;

/**
 * Created by fabien.sanglier on 9/18/18.
 */
public interface CasBackoffStrategy {
    void doWait(final long attempt);
}
