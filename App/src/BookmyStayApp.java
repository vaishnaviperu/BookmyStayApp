import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite",  2);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setRoomCount(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean dataFound = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0].trim();
                    int count       = Integer.parseInt(parts[1].trim());
                    inventory.setRoomCount(roomType, count);
                    dataFound = true;
                }
            }
            if (dataFound) {
                System.out.println("Inventory loaded successfully.");
            } else {
                System.out.println("No valid inventory data found. Starting fresh.");
            }
        } catch (IOException e) {
            System.out.println("No valid inventory data found. Starting fresh.");
        }
    }
}

public class BookmyStayApp{

    public static void main(String[] args) {

        System.out.println("System Recovery");

        RoomInventory inventory             = new RoomInventory();
        FilePersistenceService persistence  = new FilePersistenceService();

        String filePath = "inventory.txt";

        persistence.loadInventory(inventory, filePath);

        System.out.println("\nCurrent Inventory:");
        System.out.println("Single: " + inventory.getAvailableCount("Single"));
        System.out.println("Double: " + inventory.getAvailableCount("Double"));
        System.out.println("Suite: "  + inventory.getAvailableCount("Suite"));

        persistence.saveInventory(inventory, filePath);
    }
}