package com.curtisnewbie.chat;

import javax.websocket.Session;

/**
 * <p>
 * Representation of a member in a room. A member will contain a {@code Session}
 * that is used to broadcast messages and a {@code name} that should but need
 * not to uniquely identify the member.
 * </p>
 * <p>
 * A {@code Member} should be created using the factory method
 * {@link Member#of(Session, String)}
 * </p>
 */
public class Member {

    private Session session;
    private String name;

    private Member(Session session, String name) {
        this.session = session;
        this.name = name;
    }

    private Member() {
    }

    /**
     * Create a new Member
     * 
     * @param session
     * @param name
     * @return {@code NULL} if the session or name is null or empty.
     */
    public static Member of(Session session, String name) {
        if (session == null || name == null || name.trim().isEmpty())
            return null;
        else
            return new Member(session, name);
    }

    public Session getSession() {
        return this.session;
    }

    public String getName() {
        return this.name;
    }
}