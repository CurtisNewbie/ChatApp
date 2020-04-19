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
        if (room != null && (member = Member.of(session, username)) != null) {
            if (room.addMember(member))
                logger.info(member.getName() + " : " + room.getRoomKey());
        }
    }

    @OnClose
    public void onClose(@PathParam("roomkey") String roomKey, @PathParam("username") String username) {
        Room room = rooms.getRoom(roomKey);
        if (room != null) {
            logger.info("Member removed: " + username);
            room.removeMember(username);
        }
    }

    @OnError
    public void onError(@PathParam("roomkey") String roomKey, @PathParam("username") String username,
            Throwable throwable) {
        Room room = rooms.getRoom(roomKey);
        if (room != null) {
            logger.info("Error Member removed: " + username);
            room.broadcast(String.format("Error: User '%s' left the room.", username));
            room.removeMember(username);
        }
    }

    @OnMessage
    public void onMessage(String msg, @PathParam("roomkey") String roomKey, @PathParam("username") String username) {
        Room room = rooms.getRoom(roomKey);
        if (room != null) {
            logger.info("Send msg" + msg + " " + username);
            room.sendMsg(username, msg);
        }
    }
}