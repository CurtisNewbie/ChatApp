package com.curtisnewbie.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;

/**
 * A class that manages a {@code Map} of {@code Room}(s) created in this
 * application. A seperate Thread is created to remove the empty {@code Room}
 * when the {@code Room} has been created for more than 10 seconds and the
 * {@code Room} currently has no member in it.
 */
@ApplicationScoped
public class Rooms {

    @Inject
    @ConfigProperty(name = "scan.emptyRoom.seconds")
    private int scanFreq;

    @Inject
    @ConfigProperty(name = "remove.emptyRoom.olderThan.seconds")
    protected int emptyRoomOlderThan;

    @Inject
    @ConfigProperty(name = "room.key.length")
    protected int keyLen;

    @Inject
    protected RandomGenerator randomGenerator;

    @Inject
    protected Logger logger;

    // rooms where each maintains a list of session
    private Map<String, Room> rooms = new ConcurrentHashMap<>();

    /**
     * Start a new thread to watch for and remove rooms that are empty
     */
    void onStart(@Observes StartupEvent ev) {
        new Thread(() -> {
            while (true) {
                var keys = rooms.keySet();
                for (var k : keys) {
                    Room r;
                    // try to remove empty rooms that are created for more than n seconds
                    if ((r = rooms.get(k)) != null && r.isEmpty()
                            && System.currentTimeMillis() - r.getTimeCreated() > emptyRoomOlderThan * 1000) {
                        logger.info("Remove empty room: " + r.getRoomKey());
                        rooms.remove(r.getRoomKey());
                    }
                }
                try {
                    // scan every N seconds
                    Thread.sleep(scanFreq * 1000);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
        }).start();
    }

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