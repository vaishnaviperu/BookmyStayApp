import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite",  2);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public void decrementRoom(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    public void incrementRoom(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) + 1);
        }
    }

    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class CancellationService {

    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds        = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation ID not found.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        inventory.incrementRoom(roomType);
        releasedRoomIds.push(reservationId);
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations recorded.");
            return;
        }
        Stack<String> temp = (Stack<String>) releasedRoomIds.clone();
        while (!temp.isEmpty()) {
            System.out.println("Released Reservation ID: " + temp.pop());
        }
    }
}

public class BookmyStayApp{

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory             = new RoomInventory();
        CancellationService cancellService  = new CancellationService();

        String reservationId = "Single-1";
        String roomType      = "Single";

        cancellService.registerBooking(reservationId, roomType);
        inventory.decrementRoom(roomType);
        cancellService.cancelBooking(reservationId, inventory);
        cancellService.showRollbackHistory();

        System.out.println("\nUpdated Single Room Availability: " + inventory.getAvailableCount(roomType));
    }
}