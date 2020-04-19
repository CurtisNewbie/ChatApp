package com.curtisnewbie.chat;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.logging.Logger;

/**
 * Endpoint for retriving room keys for websocekt connections
 */
@RequestScoped
@Path("/room/key")
public class RoomResources {

    @Inject
    protected Rooms rooms;

    @Inject
    protected Logger logger;

    @GET
    public String getNewRoomKey() {
        logger.info("Room created");
        return rooms.createRoom();
    }
}