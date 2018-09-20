package com.github.lanimall.samples.ehcache2.service.cache;

/**
 * Created with IntelliJ IDEA.
 * User: Jeff
 * Date: 4/18/14
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TypedCacheManager<K, V> {

    void put(K key, V val);

    V get(K key);

    void remove(K key);

    void removeAll();
}
