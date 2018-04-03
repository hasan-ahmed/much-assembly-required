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

		//dependency injection of websocket into mockOnlineUser
		when(mockOnlineUser.getWebSocket()).thenReturn(mockSocket);
	}
	
	@Test
	public void test() {

		//---------------Forklift Validation-----------------
		onlineUserHashStorage.forklift();
		
		//check if there are no inconsistency after the forklift
		int inconsistency = onlineUserHashStorage.checkConsistency();
		assertEquals(inconsistency,0);
		
		
		//-------------Shadow Write Validation------------------
		
		//changes are now written directly to old storage
		//consistency should be checked after each write
		onlineUserHashStorage.add(mockOnlineUser);
		assertEquals(0, onlineUserHashStorage.checkConsistency());
		
		//-------------Shadow Read Validation-------------------
		
		//change the new storage only
		
		//check that there is 1 inconsistency and 
		assertEquals(1, onlineUserHashStorage.getReadInconsistenies());
		assertEquals(0, onlineUserHashStorage.checkConsistency());
		
	}

}
