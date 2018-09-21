package com.github.lanimall.ehcache;

import com.github.lanimall.ehcache.cas.ClusteredAtomicCounter;
import com.github.lanimall.ehcache.noncas.WrongClusteredAtomicCounter;
import com.github.lanimall.ehcache.utils.CacheController;
import com.github.lanimall.ehcache.utils.CasBackoffStrategy;
import com.github.lanimall.ehcache.utils.ExponentialWaitStrategy;
import com.github.lanimall.ehcache.utils.ThreadRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.toolkit.InvalidToolkitConfigException;
import org.terracotta.toolkit.Toolkit;
import org.terracotta.toolkit.ToolkitFactory;
import org.terracotta.toolkit.ToolkitInstantiationException;
import org.terracotta.toolkit.concurrent.ToolkitBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    //if true, it will test with the really working atomic clustered counter
    //if false, it will test with the comparative AND non-working what-seemed-like a good "atomic clustered" counter
    private static final boolean TEST_WITH_RIGHT_ATOMIC_COUNTER = true;

    private static final int TEST_NUMBER_CLIENTS = 2;
    private static final int TEST_NUMBER_THREADS = 10;
    private static final int TEST_NUMBER_ITERATIONS_PER_THREAD = 1000;

    private static final long DEFAULT_COUNTER_START = 4;
    private static final long DEFAULT_COUNTER_STRIDE = 10;
    private static final long DEFAULT_COUNTER_OPERATION_TIMEOUT = 100000; // 100sec

    private static final CasBackoffStrategy DEFAULT_CAS_BACKOFF_STRATEGY = new ExponentialWaitStrategy(1L, 50L, true);

    private static CacheController.CacheTestType[] tests = {CacheController.CacheTestType.LOCAL_HEAP, CacheController.CacheTestType.LOCAL_OFFHEAP, CacheController.CacheTestType.CLUSTERED};
//    private static CacheController.CacheTestType[] tests = {CacheController.CacheTestType.CLUSTERED};

    final Counter counterToTest;

    public MainApp(Counter counterToTest) {
        this.counterToTest = counterToTest;
    }

    public static void main(String[] args) throws InvalidToolkitConfigException, IllegalArgumentException, ToolkitInstantiationException {
        // pass in the number of object you want to generate, default is 100
        String counterName = args.length > 0 ? args[0]:"TestCounter1";
        int numberThread = Integer.parseInt(args.length > 1 ? args[1]:new Integer(TEST_NUMBER_THREADS).toString());
        int iterationCountPerThread = Integer.parseInt(args.length > 2 ? args[2]:new Integer(TEST_NUMBER_ITERATIONS_PER_THREAD).toString());

        final CacheController cacheController = new CacheController();
        Counter counterToTest;

        if(TEST_WITH_RIGHT_ATOMIC_COUNTER)
            logger.info(String.format("========>>>>>>>>> Begin Tests with the right Clustered Atomic Counter: %s ====================", ClusteredAtomicCounter.class));
        else
            logger.info(String.format("========>>>>>>>>> Begin Tests with the wrong Clustered Atomic Counter: %s ====================", WrongClusteredAtomicCounter.class));


        //run all the tests
        for(CacheController.CacheTestType testType: tests){
            logger.info(String.format("================= Begin Test: %s ====================", testType.getPropValue()));

            Toolkit toolkit = null;
            ToolkitBarrier clusteredBarrier = null;
            int barrierIndex = -1;
            try {
                cacheController.setUpCache(testType, true);

                if(testType == CacheController.CacheTestType.CLUSTERED) {
                    String tcurlStr = cacheController.getCm().getConfiguration().getTerracottaConfiguration().getUrl();
                    String[] tcurls = tcurlStr.split(",");

                    String toolkitUrl = "toolkit:terracotta://";
                    for(String url : tcurls){
                        if(toolkitUrl.length() > 0)
                            toolkitUrl += ",";
                        toolkitUrl += url;
                    }

                    //start connecting to terracotta
                    toolkit = ToolkitFactory.createToolkit(toolkitUrl);
                    clusteredBarrier = toolkit.getBarrier("test", TEST_NUMBER_CLIENTS);
                    logger.info("Waiting for peers...");
                    barrierIndex = clusteredBarrier.await();
                    logger.info("Everyone is here... Let's GO!! Current peer index is {}", barrierIndex);
                }

                if(TEST_WITH_RIGHT_ATOMIC_COUNTER)
                    counterToTest = new ClusteredAtomicCounter(cacheController.getCache(), counterName, DEFAULT_COUNTER_START, DEFAULT_COUNTER_OPERATION_TIMEOUT, DEFAULT_CAS_BACKOFF_STRATEGY);
                else
                    counterToTest = new WrongClusteredAtomicCounter(cacheController.getCache(), counterName, DEFAULT_COUNTER_START);

                MainApp mainApp = new MainApp(counterToTest);
                mainApp.run(numberThread, iterationCountPerThread);

                //if clustered, let's wait that all clients and threads are done to get the final count
                if(testType == CacheController.CacheTestType.CLUSTERED && null != clusteredBarrier) {
                    logger.info("Waiting for peers to all be finished...");
                    clusteredBarrier.await();
                }

                long finalCount = counterToTest.get();
                long expectedCount;
                if(testType == CacheController.CacheTestType.CLUSTERED) {
                    expectedCount = DEFAULT_COUNTER_START + TEST_NUMBER_CLIENTS * numberThread * iterationCountPerThread * DEFAULT_COUNTER_STRIDE;
                } else {
                    expectedCount = DEFAULT_COUNTER_START + numberThread * iterationCountPerThread * DEFAULT_COUNTER_STRIDE;
                }

                boolean expected = finalCount == expectedCount;
                logger.info("==========>>>>>>> Final counter in cache = {} -- Expected Value = {} -- {}",
                        finalCount,
                        expectedCount,
                        expected ? "Yay, it matches!!" : "Bouuuu something is not right!!"
                );
            } catch (Exception exc){
                logger.error("Error!!", exc);
                System.exit(1);
            } finally {
                if(testType == CacheController.CacheTestType.CLUSTERED && null != toolkit) {
                    toolkit.shutdown();
                }

                cacheController.tearDownCache();
            }

            logger.info(String.format("================= Ended Test: %s ====================", testType.getPropValue()));
        }

        System.exit(0);
    }

    void run(final int threadCount, final int iterationCountPerThread) {
        List<Callable<Long>>  callables = new ArrayList<Callable<Long>>();
        List<AtomicReference<Long>> callableResults = new ArrayList<AtomicReference<Long>>();
        List<AtomicReference<Class>> exceptions = new ArrayList<AtomicReference<Class>>();

        for(int i = 0; i < threadCount; i++) {
            final AtomicReference<Long> resultInc = new AtomicReference<Long>();
            final AtomicReference<Class> exceptionInc = new AtomicReference<Class>();

            callableResults.add(resultInc);
            exceptions.add(exceptionInc);
            callables.add(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    long count = 0;
                    for(int i=0;i<iterationCountPerThread;i++) {
                        count = counterToTest.addAndGet(DEFAULT_COUNTER_STRIDE);
                    }
                    return count;
                }
            });
        }

        try {
            new ThreadRunner(callables, callableResults, exceptions).runInThreads();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
