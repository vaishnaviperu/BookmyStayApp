import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) { super(message); }
}
class RoomInventory {
    private Map<String, Integer> inventory;
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 3);
        inventory.put("Double", 2);
        inventory.put("Suite",  1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class BookingRequestQueue {
    private Queue<String> requests;
    public BookingRequestQueue() {
        requests = new LinkedList<>();
    }
    public void addRequest(String request) {
        requests.add(request);
    }
}

class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        if (inventory.getAvailableCount(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }
}

public class BookmyStayApp{

    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);
        RoomInventory inventory          = new RoomInventory();
        ReservationValidator validator   = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            validator.validate(guestName, roomType, inventory);

            bookingQueue.addRequest(guestName + ":" + roomType);
            System.out.println("Booking successful for " + guestName + " - Room Type: " + roomType);

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}