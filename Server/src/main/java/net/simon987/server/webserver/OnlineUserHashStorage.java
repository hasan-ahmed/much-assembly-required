package net.simon987.server.webserver;

import org.java_websocket.WebSocket;

import java.util.HashMap;


public class OnlineUserHashStorage extends OnlineUserManager {

    private HashMap<WebSocket, OnlineUser> onlineUsersHash = new HashMap<>();

    public OnlineUser getUser(WebSocket socket) {
        return onlineUsersHash.get(socket);
    }

    public void add(OnlineUser user) {
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

}
