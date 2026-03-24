import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType  = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType()  { return roomType; }
}

class RoomInventory {
    private Map<String, Integer> inventory;
    private Map<String, Integer> allocationCount;

    public RoomInventory() {
        inventory       = new HashMap<>();
        allocationCount = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite",  2);
        allocationCount.put("Single", 0);
        allocationCount.put("Double", 0);
        allocationCount.put("Suite",  0);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            allocationCount.put(roomType, allocationCount.get(roomType) + 1);
        }
    }

    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public int getAllocationCount(String roomType) {
        return allocationCount.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requests;

    public BookingRequestQueue() {
        requests = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requests.add(reservation);
    }

    public Reservation pollRequest() {
        return requests.poll();
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }
}

class RoomAllocationService {
    private int allocationCounter;

    public RoomAllocationService() {
        allocationCounter = 1;
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        if (inventory.isAvailable(roomType)) {
            int count   = allocationCounter++;
            String roomId = roomType + "-" + count;
            inventory.decrementRoom(roomType);
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName()
                    + ", Room ID: " + roomId);
        } else {
            System.out.println("No rooms available for Guest: " + reservation.getGuestName()
                    + ", Room Type: " + roomType);
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue      = bookingQueue;
        this.inventory         = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                reservation = bookingQueue.pollRequest();
            }

            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

public class BookmyStayApp{

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        BookingRequestQueue bookingQueue       = new BookingRequestQueue();
        RoomInventory inventory                = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        bookingQueue.addRequest(new Reservation("Abhi",     "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural",    "Suite"));
        bookingQueue.addRequest(new Reservation("Subha",    "Single"));

        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println("\nRemaining Inventory:");
        System.out.println("Single: " + inventory.getAvailableCount("Single"));
        System.out.println("Double: " + inventory.getAvailableCount("Double"));
        System.out.println("Suite: "  + inventory.getAvailableCount("Suite"));
    }
}