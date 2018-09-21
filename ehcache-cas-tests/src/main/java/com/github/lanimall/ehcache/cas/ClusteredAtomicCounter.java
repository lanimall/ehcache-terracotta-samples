package com.github.lanimall.ehcache.cas;

import com.github.lanimall.ehcache.Counter;
import com.github.lanimall.ehcache.utils.CasBackoffStrategy;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * Created by fabien.sanglier on 9/20/18.
 */
public class ClusteredAtomicCounter implements Counter {
    private static final Logger logger = LoggerFactory.getLogger(ClusteredAtomicCounter.class);
    private static final boolean isDebug = logger.isDebugEnabled();

    private final Ehcache cache;
    private final String counterName;
    private final long counterInitValue;
    private final CasBackoffStrategy waitStrategy;
    private final long timeoutMillis;

    public ClusteredAtomicCounter(final Ehcache cache, final String counterName, final long counterInitValue, final long timeoutMillis, final CasBackoffStrategy waitStrategy) {
        this.cache = cache;
        this.counterName = counterName;
        this.counterInitValue = counterInitValue;
        this.waitStrategy = waitStrategy;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public long get() {
        return getCounterValueFromCache();
    }

    @Override
    public long addAndGet(long delta) throws Exception {
        return atomicMutateCounterInCache(MutationType.INCREMENT, delta);
    }

    @Override
    public long substractAndGet(long delta) throws Exception {
        return atomicMutateCounterInCache(MutationType.DECREMENT, delta);
    }

    //Main CAS loop to update the count atomically
    private long atomicMutateCounterInCache(final MutationType mutationType, long delta) throws TimeoutException {
        Long mutatedCount = null;
        long t1 = System.currentTimeMillis();
        long t2 = t1; //this ensures that the while always happen at least once!
        boolean isMutated = false;
        long attempts = 0L;
        while (!isMutated && t2 - t1 <= timeoutMillis) {
            //get the master index from cache, unless override is set
            Long countFromCache = getCounterValueFromCache();

            //create if not null
            if(null == countFromCache) {
                mutatedCount = new Long(counterInitValue);
            } else {
                mutatedCount = new Long(countFromCache);
            }

            //mutate as requested
            mutatedCount = mutationType.mutate(mutatedCount, delta);

            if(isDebug)
                logger.debug("Mutated count before CAS commit to cache: {}", mutatedCount);

            //concurrency check with CAS: let's save the initial EhcacheStreamMaster in cache, while making sure it hasn't change so far
            //if multiple threads are trying to do this replace on same key, only one thread is guaranteed to succeed here...while others will fail their CAS ops...and spin back to try again later.
            if(null == countFromCache){
                isMutated = (null == cache.putIfAbsent(new Element(counterName,mutatedCount)));
            } else {
                isMutated = cache.replace(new Element(counterName,countFromCache), new Element(counterName,mutatedCount));
            }

            if (isMutated) {
                if(isDebug)
                    logger.debug("Mutated count after CAS commit to ehcache: {}", mutatedCount);

                //at this point, the object has been changed in cache as expected
                isMutated = true;
            }

            if(!isMutated) {
                if(null != waitStrategy)
                    waitStrategy.doWait(attempts); //wait time
                t2 = System.currentTimeMillis();
                attempts++;
            }
        }

        if(isDebug)
            logger.debug("Total cas loop iterations: {}", attempts);

        //if it's not mutated at the end of all the tries and timeout, throw timeout exception
        if (!isMutated) {
            throw new TimeoutException(String.format("Could not perform operation within %d ms (timeout triggers at %d ms)", t2 - t1, timeoutMillis));
        }

        return mutatedCount;
    }

    private Long getCounterValueFromCache(){
        Long val = null;
        Element element = cache.get(counterName);
        if(null != element)
            val = (Long)element.getObjectValue();

        return val;
    }

    private enum MutationType {
        INCREMENT {
            @Override
            public long mutate(long count, long stride) {
                return (count = count + stride);
            }
        }, DECREMENT {
            @Override
            public long mutate(long count, long stride) {
                return (count = count - stride);
            }
        };

        abstract public long mutate(long count, long stride);
    }
}
