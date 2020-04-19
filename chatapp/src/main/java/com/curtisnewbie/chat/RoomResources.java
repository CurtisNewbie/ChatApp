package com.curtisnewbie.chat;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Endpoint for retriving room keys for websocekt connections
 */
@RequestScoped
@Path("/room/key")
public class RoomResources {

    @Inject
    protected Rooms rooms;

    @GET
    public String getNewRoomKey() {
        return rooms.createRoom();
    }
}