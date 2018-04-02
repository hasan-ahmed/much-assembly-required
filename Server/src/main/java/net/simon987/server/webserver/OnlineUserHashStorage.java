package net.simon987.server.webserver;

import org.java_websocket.WebSocket;

import java.util.HashMap;


public class OnlineUserHashStorage extends OnlineUserManager {

    private HashMap<WebSocket, OnlineUser> onlineUsersHash = new HashMap<>();

    public OnlineUser getUser(WebSocket socket) {
        return onlineUsersHash.get(socket);
    }

    public void add(OnlineUser user) {
    	//
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

    //return number of inconsistency
    public int checkConsistency() {
    	int inconsistencies = 0;
    	
    	for (WebSocket socket : onlineUsersHash.keySet()) {
    		OnlineUser expected = onlineUsersHash.get(socket);
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

