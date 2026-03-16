import java.util.HashMap;
import java.util.Map;

/**
 * ================================================================
 * ABSTRACT CLASS - Room
 * ================================================================
 *
 * Represents a general hotel room with common properties.
 * This class will be extended by specific room types.
 */

abstract class Room {

    protected int beds;
    protected int size;
    protected double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
    }
}

/* ================================================================
   CLASS - SingleRoom
   ================================================================ */

class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 250, 1500.0);
    }
}

/* ================================================================
   CLASS - DoubleRoom
   ================================================================ */

class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 400, 2500.0);
    }
}

/* ================================================================
   CLASS - SuiteRoom
   ================================================================ */

class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 750, 5000.0);
    }
}

/**
 * ================================================================
 * CLASS - RoomInventory
 * ================================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * This class acts as the single source of truth
 * for room availability in the hotel.
 *
 * Room pricing and characteristics are obtained
 * from Room objects, not duplicated here.
 *
 * @version 3.1
 */

class RoomInventory {

    /**
     * Stores available room counts for each room type.
     * Key - Room type name
     * Value - Available room count
     */
    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes the inventory
     * with default availability values.
     */
    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    /**
     * Initializes room availability data.
     */
    private void initializeInventory() {

        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Returns the current availability map.
     */
    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    /**
     * Updates availability for a specific room type.
     */
    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

/**
 * ================================================================
 * MAIN CLASS - UseCase3InventorySetup
 * ================================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Demonstrates how room availability is managed
 * using a centralized HashMap inventory.
 *
 * @version 3.1
 */

public class BookmyStayApp {

    public static void main(String[] args) {

        System.out.println("Hotel Room Inventory Status\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Retrieve availability from inventory
        int singleAvailable = inventory.getRoomAvailability().get("Single");
        int doubleAvailable = inventory.getRoomAvailability().get("Double");
        int suiteAvailable = inventory.getRoomAvailability().get("Suite");

        System.out.println("Single Room:");
        single.displayDetails();
        System.out.println("Available Rooms: " + singleAvailable + "\n");

        System.out.println("Double Room:");
        doubleRoom.displayDetails();
        System.out.println("Available Rooms: " + doubleAvailable + "\n");

        System.out.println("Suite Room:");
        suite.displayDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
    }
}