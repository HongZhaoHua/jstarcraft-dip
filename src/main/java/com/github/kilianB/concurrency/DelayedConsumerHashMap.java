package com.github.kilianB.concurrency;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Accepts a consumer which executes all provided tasks in a batch no more often than the delay
 * specified.
 * Tasks are labeled by id and will replace each other if they arrive within the grace period while
 * ensuring that the task scheduled last will eventually be executed.
 * 
 * <ol>
 * <li>Event( key = 1) arrives. Consumer has not executed a task therefore it is executed immediately.</li>
 * <li>Event( key = 2) arrives. Consumer is currently sleeping</li>
 * <li>Event( key = 1) arrives. Consumer is currently sleeping</li>
 * <li>Event( key = 2) arrives. Consumer is currently sleeping. The first event with key 2 is replaced and will never
 *  be executed.
 *  <li>Grace period over. Execute Event(key = 2) and Event( key = 1)</li>
 *  <li>Lay dormant until new tasks arrive</li>
 * </ol>
 * 
 * Due to the use of internal locks no polling takes place. 
 * @author Kilian
 *
 * @param <T> Type of the task the consumer will execute
 * @since 1.0.0
 */
public class DelayedConsumerHashMap<T> {

	private final HashMap<Integer, T> objects = new HashMap<>();

	/** Main lock guarding all access */
	private final ReentrantLock lock;  //Maybe support a seperate sleep timer for each id?

	/** Condition for waiting takes */
	private final Condition notEmpty;

	private Consumer<T> consumer;
	private int sleepDuration;
	
	public DelayedConsumerHashMap(Consumer<T> consumer, int sleep) {
		 lock = new ReentrantLock(true);
		 notEmpty = lock.newCondition();
		 this.consumer = consumer;
		 this.sleepDuration = sleep;
		 Thread internalThread = new Thread(() ->{
			 handleRequest(); 
		 });
		 internalThread.setDaemon(false);
		 internalThread.start();
	}

	public void put(Integer key, T object) {
		lock.lock();
		objects.put(key, object);
		notEmpty.signal();
		lock.unlock();
		
	}

	private void handleRequest() {
		try {
			lock.lock();
			if(objects.size() == 0) {
				//Spurious wakeups are no problem here 
				notEmpty.await();
			}
			for(Entry<Integer, T> entry : objects.entrySet()) {
				consumer.accept(entry.getValue());
			}
			objects.clear();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		try {
			Thread.sleep(sleepDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handleRequest();
	}
}