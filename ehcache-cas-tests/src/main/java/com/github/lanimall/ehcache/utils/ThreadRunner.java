package com.github.lanimall.ehcache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by fabien.sanglier on 9/20/18.
 */
public class ThreadRunner {
    private static final Logger logger = LoggerFactory.getLogger(ThreadRunner.class);
    public static NumberFormat formatD = new DecimalFormat("#.###");

    private final List<Callable<Long>> callables;
    private final List<AtomicReference<Long>> callableResults;
    private final List<AtomicReference<Class>> exceptions;

    public ThreadRunner(List<Callable<Long>> callables, List<AtomicReference<Long>> callableResults, List<AtomicReference<Class>> exceptions) {
        this.callables = callables;
        this.callableResults = callableResults;
        this.exceptions = exceptions;
    }

    public void runInThreads() throws InterruptedException {
        long start = 0L, end = 0L;
        logger.info("============ runInThreads ====================");

        if(null == callables)
            throw new IllegalStateException("must provides some operations to run...");

        if(null != callableResults && callables.size() != callableResults.size())
            throw new IllegalStateException("must provides the same number of callableResults as the number of callables");

        if(null != exceptions && callables.size() != exceptions.size())
            throw new IllegalStateException("must provides the same number of exception counters as the number of callables");

        final List<ThreadWorker> workerList = new ArrayList<ThreadWorker>(callables.size());
        final CountDownLatch stopLatch = new CountDownLatch(callables.size());

        //add first worker
        int count = 0;
        for(int i = 0; i < callables.size(); i++) {
            workerList.add(new ThreadWorker(callables.get(i), stopLatch, callableResults.get(i), exceptions.get(i)));
        }

        //start the workers
        start = System.nanoTime();
        for (ThreadWorker worker : workerList) {
            worker.start();
        }

        //wait that all operations are finished
        stopLatch.await();
        end = System.nanoTime();
        logger.info("Execution Time = " + formatD.format((double)(end - start) / 1000000) + " millis");
        logger.info("============ end runInThreads ====================");
    }

    public class ThreadWorker<R> extends Thread {
        private final Callable<R> callable;
        private final CountDownLatch doneLatch;
        private AtomicReference<R> callableResult;
        private AtomicReference<Class> exception;

        public ThreadWorker(Callable<R> callable, CountDownLatch doneLatch, AtomicReference<R> callableResult, AtomicReference<Class> exception) {
            super();
            this.callable = callable;
            this.doneLatch = doneLatch;
            this.callableResult = callableResult;
            this.exception = exception;
        }

        @Override
        public void run() {
            try {
                callableResult.set(callable.call());
            } catch (Exception e) {
                if(null != exception)
                    exception.set(e.getClass());
                logger.debug(e.getMessage(),e);
            } finally{
                doneLatch.countDown();
            }
        }
    }
}
