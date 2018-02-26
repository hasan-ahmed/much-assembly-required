package net.simon987.server.game;

import com.mongodb.*;
import net.simon987.server.ServerConfiguration;
import net.simon987.server.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.omg.CORBA.TIMEOUT;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameUniverseTest {

    GameUniverse gameUniverse;

    @Before
    public void setUp() {
        ServerConfiguration configMock = Mockito.mock(ServerConfiguration.class);
        Mockito.when(configMock.getInt("wg_centerPointCountMin")).thenReturn(5);
        Mockito.when(configMock.getInt("wg_centerPointCountMax")).thenReturn(15);
        Mockito.when(configMock.getInt("wg_wallPlainRatio")).thenReturn(4);
        Mockito.when(configMock.getInt("wg_minIronCount")).thenReturn(0);
        Mockito.when(configMock.getInt("wg_maxIronCount")).thenReturn(2);
        Mockito.when(configMock.getInt("wg_minCopperCount")).thenReturn(0);
        Mockito.when(configMock.getInt("wg_maxCopperCount")).thenReturn(2);

        gameUniverse = new GameUniverse(configMock);
    }

    @Test
    public void addWorld() {

        World worldMock = Mockito.mock(World.class);
        Mockito.when(worldMock.getId()).thenReturn("w-0x31-0x31");
        gameUniverse.addWorld(worldMock);
        assertEquals(worldMock, gameUniverse.getWorldValue("w-0x31-0x31"));

    }

    @Test
    public void setMongo() {
        MongoClient mongoMock = Mockito.mock(MongoClient.class);
        gameUniverse.setMongo(mongoMock);


        Field privateMongoClient;
        MongoClient valueOfMongoClient = null;
        try {
            privateMongoClient = GameUniverse.class.getDeclaredField("mongo");
            privateMongoClient.setAccessible(true);
            valueOfMongoClient = (MongoClient) privateMongoClient.get(gameUniverse);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assertEquals(mongoMock, valueOfMongoClient);
    }


    @Test
    public void getWorld() {


////        Does not work
//          DB DbMock = Mockito.mock(DB.class);
////        MongoClient mongoMock = Mockito.mock(MongoClient.class);
////        DBCollection worldMock = DBCollection
////        DBCursor cursorMock = Mockito.mock(DBCursor.class);
////
////        Mockito.when(mongoMock.getDB("mar")).thenReturn(DbMock);
////        Mockito.when(DbMock.getCollection("world")).thenReturn(nu);
//
//
//        TileMap tileMapMock = Mockito.mock(TileMap.class);
//        World world = new World(1,1, tileMapMock);
//        gameUniverse.addWorld(world);
//        World worldOutput = gameUniverse.getWorld(1, 1, true);
//        assertNotNull(world);


    }

    @Test
    public void getLoadedWorld() {
        TileMap tileMapMock = Mockito.mock(TileMap.class);
        World world = new World(1, 1, tileMapMock);
        gameUniverse.addWorld(world);

        assertEquals(world, gameUniverse.getLoadedWorld(1, 1));
    }

    @Test
    public void getWorldValue() {
        int[][] t = new int[16][16];

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                t[i][j] = 0;
            }
        }
        TileMap tileMapMock = Mockito.mock(TileMap.class);
        World worldTest = new World(16, 16, tileMapMock);
        gameUniverse.addWorld(worldTest);
        Mockito.when(tileMapMock.getTiles()).thenReturn(t);

        assertEquals(worldTest, gameUniverse.getWorldValue("w-0x10-0x10"));
    }

    @Test
    public void removeWorld() {
        TileMap tileMapMock = Mockito.mock(TileMap.class);
        World worldTest = new World(1, 1, tileMapMock);
        gameUniverse.addWorld(worldTest);
        assertNotEquals(0, gameUniverse.getWorldCount());


        gameUniverse.removeWorld(worldTest);
        assertEquals(0, gameUniverse.getWorldCount());
    }

    @Test
    public void getUser() {
        User userMock = Mockito.mock(User.class);
        Mockito.when(userMock.getUsername()).thenReturn("test@test.com");

        gameUniverse.addUser(userMock);
        assertEquals(userMock, gameUniverse.getUser("test@test.com"));
    }

//    @Test
//    public void getOrCreateUser() {
//        Needs to be done
//        Mockito.when(gameUniverse.getUser("test@test.com")).thenReturn(null);
//
//    }

    @Test
    public void getObject() {
        GameUniverse uniMock = Mockito.mock(GameUniverse.class);
        GameObject objMock = Mockito.mock(GameObject.class);
        objMock.setObjectId(1);
        objMock.setX(2);
        objMock.setY(2);
        verify(objMock).setObjectId(1);
        verify(objMock).setX(2);
        verify(objMock).setY(2);
        when(uniMock.getObject(1)).thenReturn(objMock);
        assertEquals(objMock, uniMock.getObject(1));
    }

    @Test
    public void getWorlds() {

        TileMap t = new TileMap(16, 16);
        World world1 = new World(16, 16, t);
        World world2 = new World(18, 18, t);
        ConcurrentHashMap<String, World> worldCollection = new ConcurrentHashMap<>();
        worldCollection.put(world1.getId(),world1);
        worldCollection.put(world2.getId(),world2);
        this.gameUniverse.addWorld(world1);
        this.gameUniverse.addWorld(world2);
        assertEquals(worldCollection.values()+"",this.gameUniverse.getWorlds()+"");
    }

    @Test
    public void getWorldCount() {
        Random r = new Random();
        int randNum = r.nextInt(10);
        TileMap tileMapMock = Mockito.mock(TileMap.class);
        for (int i = 0; i < randNum ; i++) {

            World world = new World(i,1, tileMapMock);
            gameUniverse.addWorld(world);
        }

        Field privateCollectionWorld;
        ConcurrentHashMap<String,World> valueOfCollectionWOrld = null;
        try {
            privateCollectionWorld = GameUniverse.class.getDeclaredField("worlds");
            privateCollectionWorld.setAccessible(true);
            valueOfCollectionWOrld = (ConcurrentHashMap<String, World>) privateCollectionWorld.get(gameUniverse);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assertEquals(randNum, valueOfCollectionWOrld.size());

    }

    @Test
    public void getUsers() {
        User userMock1 = Mockito.mock(User.class);
        User userMock2 = Mockito.mock(User.class);
        Mockito.when(userMock1.getUsername()).thenReturn("test@test.com");
        Mockito.when(userMock2.getUsername()).thenReturn("test2@test.com");
        ConcurrentHashMap<String, User> userCollection = new ConcurrentHashMap<>();


        userCollection.put(userMock1.getUsername(),userMock1);
        userCollection.put(userMock2.getUsername(),userMock2);
        this.gameUniverse.addUser(userMock1);
        this.gameUniverse.addUser(userMock2);

        assertEquals(userCollection.values()+"",this.gameUniverse.getUsers()+"");

    }

    @Test
    public void getUserCount() {
        Random r = new Random();
        int randNum = r.nextInt(10);
        for (int i = 0; i < randNum ; i++) {
            User userMock = Mockito.mock(User.class);
            Mockito.when(userMock.getUsername()).thenReturn("test"+i+"@test.com");
            gameUniverse.addUser(userMock);
        }

        Field privateCollectionUsers;
        ConcurrentHashMap<String,User> valueOfCollectionUsers = null;
        try {
            privateCollectionUsers = GameUniverse.class.getDeclaredField("users");
            privateCollectionUsers.setAccessible(true);
            valueOfCollectionUsers = (ConcurrentHashMap<String, User>) privateCollectionUsers.get(gameUniverse);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assertEquals(randNum, valueOfCollectionUsers.size());

    }

    @Test
    public void getNextObjectId() {
        GameUniverse uniMock = Mockito.mock(GameUniverse.class);
        uniMock.setNextObjectId(1);
        verify(uniMock).setNextObjectId(1);
        when(uniMock.getNextObjectId()).thenReturn((long)1);
        assertEquals(uniMock.getNextObjectId(), gameUniverse.getNextObjectId());
    }

    @Test
    public void setNextObjectId() {
        Random r = new Random();
        int objectId = r.nextInt(100-1) + 1;
        int nextObjectId = objectId + 1;

        GameUniverse uniMock = Mockito.mock(GameUniverse.class);
        uniMock.setNextObjectId(objectId);
        when(uniMock.getNextObjectId()).thenReturn((long)nextObjectId);

        this.gameUniverse.setNextObjectId(objectId);

        assertEquals(uniMock.getNextObjectId(), gameUniverse.getNextObjectId());
    }

    @Test
    public void getGuestUsername() {
        Random r = new Random();
        int guestNumber = r.nextInt(10000-1) + 1;

        String guestUsername = "guest" + String.valueOf(guestNumber);
        User guestUser = Mockito.mock(User.class);
        Mockito.when(guestUser.getUsername()).thenReturn(guestUsername);

        this.gameUniverse.addUser(guestUser);

        assertNotEquals(guestUsername, this.gameUniverse.getGuestUsername());
    }

    @Test
    public void addUser() {
        User userMock = Mockito.mock(User.class);
        Mockito.when(userMock.getUsername()).thenReturn("test@test.com");

        gameUniverse.addUser(userMock);
        assertNotEquals(0, gameUniverse.getUserCount());

    }

    @Test
    public void removeUser() {
        User userMock = Mockito.mock(User.class);
        Mockito.when(userMock.getUsername()).thenReturn("test@test.com");

        gameUniverse.addUser(userMock);
        assertNotEquals(0, gameUniverse.getUserCount());

        gameUniverse.removeUser(userMock);
        assertEquals(0, gameUniverse.getUserCount());
    }

}
