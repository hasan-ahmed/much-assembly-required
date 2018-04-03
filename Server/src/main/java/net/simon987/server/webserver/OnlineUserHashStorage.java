package net.simon987.server.webserver;

import org.java_websocket.WebSocket;

import java.util.HashMap;


public class OnlineUserHashStorage extends OnlineUserManager implements Runnable {

    private HashMap<WebSocket, OnlineUser> onlineUsersHash;
    private  int readInconsistenies = 0;
    private int inconsistencies = 0;
    private int checkConsistencyRunCounter = 0;
    private Thread t = new Thread();
    
    //constructor
    public OnlineUserHashStorage(){
        this.onlineUsersHash = new HashMap<>();
    	
    }

    public OnlineUser getUser(WebSocket socket) {
        OnlineUser expected = super.getUser(socket);

        // Shadow Read
        OnlineUser actual = onlineUsersHash.get(socket);

        if(!expected.equals(actual)){
            readInconsistenies++;
            // Fix inconsistency
            onlineUsersHash.put(socket, expected);
            violation(socket,expected,actual);
        }

        return expected;
    }

    public void add(OnlineUser onlineUser) {
        super.add(onlineUser);
        // Shadow Write
        onlineUsersHash.put(onlineUser.getWebSocket(), onlineUser);
        t.start();
    }

    public void remove(OnlineUser onlineUser) {
        onlineUsersHash.remove(onlineUser.getWebSocket());
        t.start();
    }

    //super.getOnlineUsers return the arraylist
    public void forklift() {
        for (OnlineUser user : super.getOnlineUsers()) {
            onlineUsersHash.put(user.getWebSocket(), user);
        }
        t.start();
    }

    //return number of inconsistency
    public int checkConsistency() {
    	
    	for (WebSocket socket : onlineUsersHash.keySet()) {
    		//get from OnlineUserManager (old storage)
    		OnlineUser expected = super.getUser(socket);
    		
    		//get from the hash map (new storage)
    		OnlineUser actual = onlineUsersHash.get(socket);

    		this.checkConsistencyRunCounter++;
    		
    		//compare each param of actual and param
    		if(!actual.equals(expected)) {
    			this.inconsistencies++;
    			onlineUsersHash.put(socket,expected);
    			violation(socket, expected, actual);	
    		}
    		
    	}
    	return this.inconsistencies;
    }
    private void violation(WebSocket socket, OnlineUser expected, OnlineUser actual) {
		System.out.println("Consistency Violation!\n" + 
				"socket = " + socket +
				"\n\t expected = " + expected
				+ "\n\t actual = " + actual);
	}

	public int getReadInconsistenies() {
        return readInconsistenies;
    }

    @Override
    public void run() {
        checkConsistency();
    }
}

