import java.util.HashMap;
import java.util.Map;

/**
 * Book My Stay App - Use Case 3
 * Centralized Room Inventory using HashMap
 *
 * @author YourName
 * @version 1.0
 */

// Inventory Manager Class
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Add room type with count
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase or decrease)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + change;

        if (updated < 0) {
            System.out.println("Not enough rooms available for " + roomType);
        } else {
            inventory.put(roomType, updated);
        }
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\n===== Current Room Inventory =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// Main Application
publicclass BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Welcome to Book My Stay =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Simulate booking
        System.out.println("\nBooking 2 Single Rooms...");
        inventory.updateAvailability("Single Room", -2);

        // Simulate cancellation
        System.out.println("Cancelling 1 Double Room...");
        inventory.updateAvailability("Double Room", +1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nThank you for using Book My Stay!");
    }
}