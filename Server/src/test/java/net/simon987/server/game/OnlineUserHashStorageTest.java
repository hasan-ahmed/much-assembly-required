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
	//not mocked yet
	
	//new storage
	OnlineUserHashStorage onlineUserHashStorage;
	
	@Before
	public void setup() {
		
		HashMap<WebSocket, OnlineUser> onlineUsersHash = null;
		onlineUserHashStorage = new OnlineUserHashStorage();
		
		onlineUsersHash.put(mockSocket, mockOnlineUser);
	}
	
	@Test
	public void test() {
		
		onlineUserHashStorage.forklift();
		
		//check if there are no inconsistency after the forklift
		int inconsistency = onlineUserHashStorage.checkConsistency();
		assertEquals(inconsistency,0);
	}

}
