package com.curtisnewbie.chat;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

/**
 * Endpoint for retriving room keys for websocekt connections and fetching a
 * {@code List} of {@code Member}(s) in the {@code Room}.
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
        String key = rooms.createRoom();
        logger.info(String.format("Room created: %s", key));
        return key;
    }

    @GET
    @Path("/{roomkey}/members")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getRoomMembers(@PathParam(value = "roomkey") String roomKey) {
        logger.info(String.format("Get room members: %s", roomKey));
        Room r;
        if ((r = rooms.getRoom(roomKey)) != null) {
            return r.getMembers();
        } else {
            return new ArrayList<String>();
        }
    }
}