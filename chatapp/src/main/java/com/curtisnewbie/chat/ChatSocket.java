package com.curtisnewbie.chat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

/**
 * WebSocket Connection for Chat
 */
@ServerEndpoint("/chat/room/{roomkey}/name/{username}")
@ApplicationScoped
public class ChatSocket {

    @Inject
    protected Rooms rooms;

    @Inject
    protected Logger logger;

    @OnOpen
    public void onOpen(Session session, @PathParam("roomkey") String roomKey, @PathParam("username") String username) {
        Room room = rooms.getRoom(roomKey);
        Member member;
        if (room != null && (member = Member.of(session, username)) != null)
            if (room.addMember(member))
                room.broadcast(String.format("Welcome! '%s' joined the chat!", member.getName()));
    }

    @OnClose
    public void onClose(@PathParam("roomkey") String roomKey, @PathParam("username") String username) {
        Room room = rooms.getRoom(roomKey);
        if (room != null) {
            if (room.removeMember(username) != null)
                room.broadcast(String.format("User '%s' has left the room.", username));
        }
    }

    @OnError
    public void onError(@PathParam("roomkey") String roomKey, @PathParam("username") String username,
            Throwable throwable) {
        Room room = rooms.getRoom(roomKey);
        if (room != null) {
            logger.error(
                    String.format("Error, member '%s' removed. Error Message: '%s'", username, throwable.getMessage()));
            if (room.removeMember(username) != null)
                room.broadcast(String.format("Error: User '%s' has left the room.", username));
        }
    }

    @OnMessage
    public void onMessage(String msg, @PathParam("roomkey") String roomKey, @PathParam("username") String username) {
        Room room = rooms.getRoom(roomKey);
        if (room != null)
            room.sendMsg(username, msg);
    }
}