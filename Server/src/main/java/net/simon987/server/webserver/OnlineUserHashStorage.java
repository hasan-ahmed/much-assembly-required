package net.simon987.server.webserver;

import org.java_websocket.WebSocket;

import java.util.HashMap;


public class OnlineUserHashStorage extends OnlineUserManager {

    private HashMap<WebSocket, OnlineUser> onlineUsersHash = new HashMap<>();
    
    //constructor
    public OnlineUserHashStorage(){
    	
    }
    public OnlineUser getUser(WebSocket socket) {
        return onlineUsersHash.get(socket);
    }

    public void add(OnlineUser user) {
    	//Map.Entry<K,V> from https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
    	//socket would be the key here
        onlineUsersHash.put(user.getWebSocket(), user);
    }

    public void remove(OnlineUser user) {
        onlineUsersHash.remove(user.getWebSocket());
    }

    public void forklift() {
        for (OnlineUser user : getOnlineUsers()) {
            onlineUsersHash.put(user.getWebSocket(), user);
        }
    }
    //get OnlineUser from the OnlineUserManager ArrayList
    public OnlineUser User(WebSocket socket) {
    	return super.getUser(socket);
    }


    //return number of inconsistency
    public int checkConsistency() {
    	int inconsistencies = 0;
    	
    	for (WebSocket socket : onlineUsersHash.keySet()) {
    		//get from OnlineUserManager (old storage)
    		OnlineUser expected = User(socket);
    		
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
}

