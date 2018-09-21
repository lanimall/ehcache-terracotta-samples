package com.github.lanimall.ehcache.noncas;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.lanimall.ehcache.Counter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by fabien.sanglier on 9/20/18.
 */
public class WrongClusteredAtomicCounter implements Counter {
    private static final Logger logger = LoggerFactory.getLogger(WrongClusteredAtomicCounter.class);
    private static final boolean isDebug = logger.isDebugEnabled();

    private final Ehcache cache;
    private final String counterName;
    private final long counterInitValue;

    public WrongClusteredAtomicCounter(final Ehcache cache, final String counterName, final long counterInitValue) {
        this.cache = cache;
        this.counterName = counterName;
        this.counterInitValue = counterInitValue;
    }

    @Override
    public long get() {
        AtomicLong mutatedCount = getCounterValueFromCache();
        return (null != mutatedCount)?mutatedCount.get():0;
    }

    @Override
    public long addAndGet(long delta) throws Exception {
        nonAtomicMutateCounterInCache(MutationType.INCREMENT, delta);
        AtomicLong mutatedCount = getCounterValueFromCache();
        return (null != mutatedCount)?mutatedCount.get():0;
    }

    @Override
    public long substractAndGet(long delta) throws Exception {
        nonAtomicMutateCounterInCache(MutationType.DECREMENT, delta);
        AtomicLong mutatedCount = getCounterValueFromCache();
        return (null != mutatedCount)?mutatedCount.get():0;
    }

    //non atomic increment and put
    private void nonAtomicMutateCounterInCache(final MutationType mutationType, long delta) {
        AtomicLong mutatedCount;
        AtomicLong countFromCache = getCounterValueFromCache();

        //create if not null
        if(null == countFromCache) {
            mutatedCount = new AtomicLong(counterInitValue);
        } else {
            mutatedCount = countFromCache;
        }

        if(mutationType == MutationType.INCREMENT){
            mutatedCount.addAndGet(delta);
        } else if (mutationType == MutationType.DECREMENT){
            mutatedCount.addAndGet(-1 * delta);
        }
        cache.put(new Element(counterName, mutatedCount));
    }

    private AtomicLong getCounterValueFromCache(){
        AtomicLong val = null;
        Element element = cache.get(counterName);
        if(null != element)
            val = (AtomicLong)element.getObjectValue();

        return val;
    }

    private enum MutationType {
        INCREMENT, DECREMENT;
    }
}
