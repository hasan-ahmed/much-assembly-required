package net.simon987.server.event;

import net.simon987.server.GameServer;
import net.simon987.server.ServerConfiguration;
import net.simon987.server.assembly;
import net.simon987.server.user;
import net.simon987.server.assembly.DefaultInstructionSet;
import net.simon987.server.assembly.DefaultRegisterSet;
import net.simon987.server.game.ControllableUnit;
import net.simon987.server.user.User;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CpuInitialisationEventTest {

	@Mock
	private MongoClient mockClient;
	@Mock
	private MongoCollection mockCollection;
	@Mock
	private MongoDatabase mockDB;
	@InjectMocks
	private DriverWrapper wrapper;
	
	 @Before
	    public void setUp() {
		
		 when(mockClient.getDatabase(anyString())).thenReturn(mockDB);
		 when(mockDB.getCollection(anyString())).thenReturn(mockCollection);
		 wrapper.init();
		 MockitoAnnotations.initMocks(this);
		 

	     
	     
	 }

	@Test
	public void getUserTest() {
		
		FindIterable iterable = mock(FindIterable.class);
		MongoCursor cursor = mock(MongoCursor.class);
		
		User mockuser = new User((ControllableUnit) GameServer.INSTANCE.getGameUniverse().getObject((long) obj.get("controlledUnit"))
		Mockito.mock(null, mockuser.getUsername()).thenReturn("test@test.com");
		Mockito.mock(null, mockuser.getUserCode()).thenReturn("mov	ax,'00'");

		
		when(cursor.next()).thenReturn(bob);

		List<User> list = wrapper.findByUsername("test@test.com");

		assertEquals(mockuser.getUsername(), list.get(0));
		
	}

}
