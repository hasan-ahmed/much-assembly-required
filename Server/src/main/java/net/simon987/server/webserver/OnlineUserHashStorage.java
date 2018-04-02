package net.simon987.server.webserver;

import org.java_websocket.WebSocket;

import java.util.HashMap;


public class OnlineUserHashStorage extends OnlineUserManager {

    private HashMap<WebSocket, OnlineUser> onlineUsersHash;
    public  int readInconsistenies = 0;
    
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
            add(expected);
            violation(socket,expected,actual);
        }

        return expected;
    }

    public void add(OnlineUser onlineUser) {
        super.add(onlineUser);
        // Shadow Write
        onlineUsersHash.put(onlineUser.getWebSocket(), onlineUser);
    }

    public void remove(OnlineUser onlineUser) {
        onlineUsersHash.remove(onlineUser.getWebSocket());
    }

    public void forklift() {
        for (OnlineUser user : super.getOnlineUsers()) {
            onlineUsersHash.put(user.getWebSocket(), user);
        }
    }

    //return number of inconsistency
    public int checkConsistency() {
    	int inconsistencies = 0;
    	
    	for (WebSocket socket : onlineUsersHash.keySet()) {
    		//get from OnlineUserManager (old storage)
    		OnlineUser expected = super.getUser(socket);
    		
    		//get from the hash map (new storage)
    		OnlineUser actual = onlineUsersHash.get(socket);
    		
    		//compare each param of actual and param
    		if(actual != expected) {
    			inconsistencies++;
    			violation(socket, expected, actual);	
    		}
    		
    	}
    	return inconsistencies;
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
}

