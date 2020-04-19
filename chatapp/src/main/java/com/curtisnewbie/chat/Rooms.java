package com.curtisnewbie.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Rooms {

    // rooms where each maintains a list of session
    private Map<String, Room> rooms = new ConcurrentHashMap<>();

    @Inject
    protected RandomGenerator randomGenerator;

    @Inject
    @ConfigProperty(name = "KEY_LENGTH")
    protected int keyLen;

    /**
     * Create a new {@code Room}
     * 
     * @return key of the {@code Room}
     */
    public String createRoom() {
        String key;
        while (rooms.containsKey(key = randomGenerator.randomStr(keyLen)))
            ;
        Room room = new Room(key);
        rooms.put(key, room);
        return room.getRoomKey();
    }

    /**
     * Remove a {@code Room} by its key
     * 
     * @param key of the {@code Room}
     */
    public void removeRoom(String key) {
        rooms.remove(key);
    }

    /**
     * Get a {@code Room} by its key
     * 
     * @param key of the {@code Room}
     * @return {@code Room} or {@code Null} if not found
     */
    public Room getRoom(String key) {
        return rooms.get(key);
    }

}