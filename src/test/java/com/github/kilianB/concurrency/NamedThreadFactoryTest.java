package com.github.kilianB.concurrency;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class NamedThreadFactoryTest {

	@Test
	void createsNonDaemonThread() {
		NamedThreadFactory factory = new NamedThreadFactory();
		Thread t = factory.newThread(() -> {});
		assertFalse(t.isDaemon());
	}
	
	@Test
	void createsDaemonThread() {
		NamedThreadFactory factory = new NamedThreadFactory(true);
		Thread t = factory.newThread(() -> {});
		assertTrue(t.isDaemon());
	}

	@Test
	void defaultExceptionIsCaught() {
		NamedThreadFactory factory = new NamedThreadFactory();
		Thread t = factory.newThread(() -> {
			throw new IllegalArgumentException();
		});
		t.start();
		// Fails if an exception is thrown
	}

	@Test
	void exceptionHandler() {
		
		CountDownLatch latch = new CountDownLatch(1);
		NamedThreadFactory factory = new NamedThreadFactory((thread, throwable) -> {
			latch.countDown();
		});
		
		Thread t = factory.newThread(() -> {
			throw new IllegalArgumentException();
		});
		t.start();
		
		try {
			assertTrue(latch.await(150, TimeUnit.MILLISECONDS));
		}catch(InterruptedException e) {
			fail("Interrupted");
		}
	}
}
