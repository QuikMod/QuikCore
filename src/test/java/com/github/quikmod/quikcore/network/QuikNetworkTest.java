/*
 */
package com.github.quikmod.quikcore.network;

import com.github.quikmod.quikcore.defaults.QuikDefaultNetwork;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryan
 */
public class QuikNetworkTest {
	
	public QuikNetworkTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of registerMessages method, of class QuikNetwork.
	 */
	@Test
	public void testRegisterMessages() {
		System.out.println("registerMessages");
		QuikNetwork instance = new QuikNetwork(new QuikDefaultNetwork());
		instance.registerMessage(StringMessage.class);
	}

	/**
	 * Test of onMessageRecieved method, of class QuikNetwork.
	 */
	@Test
	public void testOnMessageRecieved() {
		System.out.println("onMessageRecieved");
		QuikNetwork instance = new QuikNetwork(new QuikDefaultNetwork());
		QuikByteBuffer buffer = new JavaByteBufferWrapper(ByteBuffer.allocate(100));
		StringMessage message = new StringMessage("Testing 1, 2, 3...");
		instance.registerMessage(StringMessage.class);
		buffer.addInteger(0);
		message.toBytes(buffer);
		buffer.flip();
		instance.onMessageRecieved(buffer);
	}

	/**
	 * Test of sendMessage method, of class QuikNetwork.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSendMessage() {
		System.out.println("sendMessage");
		QuikNetwork instance = new QuikNetwork(new QuikDefaultNetwork());
		StringMessage message = new StringMessage("Testing 3, 2, 1...");
		instance.sendMessage(message);
	}
	
}
