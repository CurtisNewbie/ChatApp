package com.curtisnewbie.chat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representation of a chat room, which is uniquely identified by a
 * {@code roomKey}. A {@code Map} of {@code Member} are maintained in a
 * synchronized way, where each {@code Member} contains a name and a
 * {@code Session}.
 * 
 * @see {@link com.curtisnewbie.chat.Member}
 */
public class Room {
    /** The unique key for the room */
    private String roomKey;
    /** The members maintained in each room, each identified by its name */
    private Map<String, Member> members = new ConcurrentHashMap<>();

    /**
     * Create a new Room (with the given unique {@code key}) and a synchronized
     * {@code List} of names of members
     * 
     * @param key the unique key for the room
     */
    public Room(String key) {
        this.roomKey = key;
    }

    /**
     * Get the unique key that identify this {@code Room}
     * 
     * @return
     */
    public String getRoomKey() {
        return roomKey;
    }

    /**
     * Add a {@code Member} to this {@code Room}
     * 
     * @param member a member that should not be {@code Null}
     * @return {@code True} if the operation is successful else {@code False}
     */
    public boolean addMember(Member member) {
        if (member == null)
            return false;

        return members.putIfAbsent(member.getName(), member) == null;
    }

    /**
     * Remove a member with the {@code name}
     * 
     * @param name of the member
     * @return {@code Member} of the name or {@code Null} if not found
     */
    public Member removeMember(String name) {
        return members.remove(name);
    }

    /**
     * Send a message to all members in the room
     * 
     * @param fromMemberName the name of the member who sent the message
     * @param msg            message
     */
    public void sendMsg(String fromMemberName, String msg) {
        Member fromMember = members.get(fromMemberName);
        if (fromMember != null) {
            members.values().forEach(toMember -> {
                sendMsg(fromMember, toMember, msg);
            });
        }

    }

    /**
     * send message to the {@code toMember}, if the operation fails and
     * {@code fromMember} is not {@code Null}, a message about the failure is send
     * back to the {@code fromMember}
     * 
     * @param fromMember the {@code Member} who sends the message
     * @param toMember   the {@code Member} who receieves the message
     * @param msg        message
     */
    private void sendMsg(Member fromMember, Member toMember, String msg) {
        toMember.getSession().getAsyncRemote().sendObject(msg, result -> {
            if (result.getException() != null && fromMember != null)
                fromMember.getSession().getAsyncRemote()
                        .sendObject(String.format("Failed to send message to: '%s'", toMember.getName()));
        });
    }
}