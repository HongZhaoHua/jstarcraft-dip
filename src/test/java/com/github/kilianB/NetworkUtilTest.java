package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class NetworkUtilTest {

	private Socket inSocket;
	private Socket outSocket;

	@BeforeEach
	public void setupSocket() {
		try {
			ServerSocket ss = new ServerSocket(0);

			CountDownLatch latch = new CountDownLatch(1);
			CountDownLatch latch1 = new CountDownLatch(1);

			InetAddress localHost = InetAddress.getLocalHost();
			int localPort = ss.getLocalPort();
			new Thread(() -> {
				latch.countDown();
				try {
					inSocket = ss.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
				latch1.countDown();
			}).start();

			latch.await();
			outSocket = new Socket(localHost, localPort);
			latch1.await();
			ss.close();
		} catch (IOException | InterruptedException io) {
			io.printStackTrace();
		}
	}
	

	@Test
	public void collectSocket() {
		// pipied output stream
		try {
			String testMessage = "HelloWorld1234";
			OutputStream os = outSocket.getOutputStream();
			os.write(testMessage.getBytes());
			os.flush();
			String readData = NetworkUtil.collectSocket(inSocket);
			assertEquals(testMessage, readData);
			assertFalse(inSocket.isClosed());
		} catch (IOException io) {
			fail("IO Exception occured. " + io);
		}
	}

	@Test
	public void collectSocketTimeout() {
		// pipied output stream
		try {
			String testMessage = "HelloWorld1234";
			OutputStream os = outSocket.getOutputStream();
			os.write(testMessage.getBytes());
			os.flush();
			int originalTimeout = 400;
			inSocket.setSoTimeout(originalTimeout);
			String readData = NetworkUtil.collectSocket(inSocket, 40);
			assertEquals(testMessage, readData);
			assertFalse(inSocket.isClosed());
			assertEquals(originalTimeout, inSocket.getSoTimeout());
		} catch (IOException io) {
			fail("IO Exception occured. " + io);
		}
	}

	@Test
	public void collectSocketAndClose() {
		// pipied output stream
		try {
			String testMessage = "HelloWorld1234";
			OutputStream os = outSocket.getOutputStream();
			os.write(testMessage.getBytes());
			os.flush();
			int originalTimeout = 400;
			inSocket.setSoTimeout(originalTimeout);
			String readData = NetworkUtil.collectSocketAndClose(inSocket);
			assertEquals(testMessage, readData);
			assertTrue(inSocket.isClosed());
		} catch (IOException io) {
			fail("IO Exception occured. " + io);
		}
	}

	@AfterEach
	public void closeSockets() {
		try {
			inSocket.close();
			outSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void publicIpAddress(){
		try {
			NetworkUtil.resolvePublicAddress();
		}catch(IOException io) {
			fail(io);
		}
		
	}
	

}
