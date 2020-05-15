package com.github.kilianB.concurrency;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Analogous implementation of the default thread factory used by java
 * executors. This Factory allows to set the naming pattern, daemon support and
 * exception handlers for all threads spawned. The current thread group is
 * inherited.
 * 
 * @author Kilian
 * @since 1.1.0
 */
public class NamedThreadFactory implements ThreadFactory {

	private static final Logger LOGGER = Logger.getLogger(NamedThreadFactory.class.getName());
	private static final AtomicInteger poolNumber = new AtomicInteger(1);

	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	private final boolean daemon;

	private UncaughtExceptionHandler handler = (thread, throwable) -> {
		LOGGER.severe("Uncaught exception in: " + thread + "Throwable: " + throwable);
	};

	/**
	 * A thread factory creating non daemon threads. A default uncaught exception
	 * handler is attached to every thread spawned.
	 */
	public NamedThreadFactory() {
		this("");
	}

	/**
	 * A thread factory spawning threads with the threads factory default uncaught
	 * exception handler.
	 * 
	 * @param daemon weather or not the created threads are daemon threads.
	 */
	public NamedThreadFactory(boolean daemon) {
		this("", daemon);
	}

	/**
	 * A thread factory creating non daemon threads.
	 * 
	 * @param handler Exception handler attached to every created thread
	 */
	public NamedThreadFactory(UncaughtExceptionHandler handler) {
		this("");
		this.handler = handler;
	}

	/**
	 * A thread factory creating non daemon threads. The thread factories default
	 * uncaught exception handler is attached to every thread created.
	 * 
	 * @param namePrefix prefixed to name threads created by this factory. Threads
	 *                   will be named namePrefix-thread#. If null or empty a
	 *                   pattern of pool-pool#-thread# is applied.
	 */
	public NamedThreadFactory(String namePrefix) {
		this(namePrefix, false);
	}

	/**
	 * Create a new thread factory with advanced settings. A default uncaught
	 * exception handler is attached to every thread created.
	 * 
	 * @param namePrefix prefixed to name threads created by this factory. Threads
	 *                   will be named namePrefix-thread#. If null or empty a
	 *                   pattern of pool-pool#-thread# is applied.
	 * @param daemon     weather or not the created threads are daemon threads.
	 */
	public NamedThreadFactory(String namePrefix, boolean daemon) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		if (namePrefix == null || namePrefix.isEmpty()) {
			this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		} else {
			this.namePrefix = namePrefix + "-";
		}
		this.daemon = daemon;
	}

	/**
	 * A thread factory creating non daemon threads
	 * 
	 * @param namePrefix namePrefix prefixed to name threads created by this
	 *                   factory. Threads will be named namePrefix-thread#. If null
	 *                   or empty a pattern of pool-pool#-thread# is applied.
	 * @param handler    Exception handler attached to every created thread
	 */
	public NamedThreadFactory(String namePrefix, UncaughtExceptionHandler handler) {
		this(namePrefix);
		this.handler = handler;
	}

	/**
	 * A thread factory with advanced settings.
	 * 
	 * @param namePrefix namePrefix prefixed to name threads created by this
	 *                   factory. Threads will be named namePrefix-thread#. If null
	 *                   or empty a pattern of pool-pool#-thread# is applied.
	 * @param handler    Exception handler attached to every created thread
	 * @param daemon     weather or not the created threads are daemon threads.
	 */
	public NamedThreadFactory(String namePrefix, boolean daemon, UncaughtExceptionHandler handler) {
		this(namePrefix, daemon);
		this.handler = handler;
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(daemon);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		t.setUncaughtExceptionHandler(handler);
		return t;
	}

}