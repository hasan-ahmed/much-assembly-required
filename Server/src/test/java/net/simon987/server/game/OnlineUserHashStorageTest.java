package net.simon987.server.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import net.simon987.server.webserver.OnlineUser;
import net.simon987.server.webserver.OnlineUserHashStorage;

public class OnlineUserHashStorageTest {

	@Mock
	OnlineUser mockOnlineUser = mock(OnlineUser.class);
	
	@Mock
	WebSocket mockSocket = mock(WebSocket.class);
	
	
	//new storage
	OnlineUserHashStorage onlineUserHashStorage;
	
	@Before
	public void setup() {
		onlineUserHashStorage = new OnlineUserHashStorage();
		HashMap<WebSocket, OnlineUser> onlineUsersHash = new HashMap<>();
		onlineUsersHash.put(mockSocket, mockOnlineUser);
	}
	
	@Test
	public void testForklift() {
		//---------------Forklift Validation-----------------
		onlineUserHashStorage.forklift();
		
		//check if there are no inconsistency after the forklift
		int inconsistency = onlineUserHashStorage.checkConsistency();
		assertEquals(inconsistency,0);
	}
	
	@Test
	public void testShadowWrite() {
		
		//-------------Shadow Write Validation------------------
		
		//changes are now written directly to old storage
		//consistency should be checked after each write
		onlineUserHashStorage.add(mockOnlineUser);
		assertEquals(0, onlineUserHashStorage.checkConsistency());
	}
	
	@Test
	public void testShadowRead() {
		//-------------Shadow Read Validation-------------------
		
		//change the new storage only
		when(onlineUserHashStorage.getUser(mockSocket)).thenReturn(mockOnlineUser);
		
		//make the shadow read and check if method for checking read inconsistency has been called
		onlineUserHashStorage.getUser(mockSocket);
		verify(onlineUserHashStorage).getReadInconsistenies();
		
		//check that there is 1 inconsistency and inconsistency fix
		assertEquals(1, onlineUserHashStorage.getReadInconsistenies());
		assertEquals(0, onlineUserHashStorage.checkConsistency());
		
	}

}
