import java.util.*;

/*
 * Use Case 6: Reservation Confirmation & Room Allocation
 */

class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRooms(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

class RoomAllocationService {

    /*
     * Stores all allocated room IDs
     * to prevent duplicates.
     */
    private Set<String> allocatedRooms;

    /*
     * Stores assigned room IDs by room type
     * Key -> Room Type
     * Value -> Set of Room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRooms = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /*
     * Confirms booking request by assigning
     * a unique room ID and updating inventory.
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        if (!inventory.isAvailable(roomType)) {
            System.out.println("No rooms available for " + roomType);
            return;
        }

        String roomId = generateRoomId(roomType);

        allocatedRooms.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        inventory.decrement(roomType);

        System.out.println(
                "Booking confirmed for Guest: "
                        + reservation.getGuestName()
                        + ", Room ID: "
                        + roomId);
    }

    /*
     * Generates a unique room ID
     * Example: Single-1, Single-2
     */
    private String generateRoomId(String roomType) {

        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());

        int nextId = assignedRoomsByType.get(roomType).size() + 1;

        String roomId = roomType + "-" + nextId;

        while (allocatedRooms.contains(roomId)) {
            nextId++;
            roomId = roomType + "-" + nextId;
        }

        return roomId;
    }
}

/*
 * MAIN CLASS
 */
public class BookmyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        RoomInventory inventory = new RoomInventory();

        inventory.addRooms("Single", 5);
        inventory.addRooms("Suite", 2);

        Queue<Reservation> requestQueue = new LinkedList<>();

        requestQueue.add(new Reservation("Abhi", "Single"));
        requestQueue.add(new Reservation("Subha", "Single"));
        requestQueue.add(new Reservation("Vanmathi", "Suite"));

        RoomAllocationService allocationService = new RoomAllocationService();

        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.poll();
            allocationService.allocateRoom(reservation, inventory);
        }
    }
}