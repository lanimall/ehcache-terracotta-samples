package org.terracotta;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.terracotta.toolkit.InvalidToolkitConfigException;
import org.terracotta.toolkit.Toolkit;
import org.terracotta.toolkit.ToolkitFactory;
import org.terracotta.toolkit.ToolkitInstantiationException;
import org.terracotta.toolkit.concurrent.ToolkitBarrier;
import org.terracotta.toolkit.concurrent.atomic.ToolkitAtomicLong;
import org.terracotta.toolkit.concurrent.locks.ToolkitLock;

public class MainApp {
	public static void main(String[] args) throws InvalidToolkitConfigException, IllegalArgumentException, ToolkitInstantiationException {
		//start connecting to terracotta
		Toolkit toolkit = ToolkitFactory.createToolkit("toolkit:terracotta://tcvm:9510");
		
		//Get a clustered barrier with 4 parties to simulate 4 clients coming up at once, stressing race conditions if any
		//NOTE: this is just to TEST, not needed for the exercise of having only 1 node print something
		useClusteredBarrierForTestingPurpose(toolkit, "barrierName", 4);

		//get (or create) an instance of a distributed lock
		ToolkitLock clusteredLock = toolkit.getLock("myWriteLock");
		
		//get (or create) an instance of an atomic counter - 
		//we will increment that counter to save the state long term or make sure no other clients connecting later can perform that unique operation (until Terracotta is restarted)
		ToolkitAtomicLong counter = toolkit.getAtomicLong("myAtomicCounter");

		System.out.println("counter value:" + counter.intValue());
		
		//only print something if the distributed counter is 0
		//if the counter is already at 1, no need to lock
		if (counter.intValue() == 0) {
			clusteredLock.lock(); // exclusive write lock
			try {
				if (counter.intValue() == 0) {
					System.out.println("!!My unique operation accross the cluster!!!");
					counter.incrementAndGet();
				}
			} finally {
				clusteredLock.unlock();
			}
		}
		
		//do something else...
	}
	
	private static void useClusteredBarrierForTestingPurpose(Toolkit toolkit, String barrierName, int numberOfParties){
		ToolkitBarrier clusteredBarrier = toolkit.getBarrier(barrierName, numberOfParties);
		
		System.out.println("Waiting for peers...at most 10 seconds");
		try {
			int index = clusteredBarrier.await(10, TimeUnit.SECONDS);
			System.out.println("Everyone is here... Let's GO!!");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			e1.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		System.out.println("Everyone is here...or timeout fired. Let's GO!!");
	}
}
