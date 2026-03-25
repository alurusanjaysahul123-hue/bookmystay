import java.io.*;
import java.util.*;

// ---------- Serializable Booking ----------
class SerializableBooking implements Serializable {
    private static final long serialVersionUID = 1L;
    private String guestName;
    private String roomType;
    private String roomId;

    public SerializableBooking(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    public void display() {
        System.out.println("Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// ---------- Serializable Inventory ----------
class SerializableInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> roomAvailability;

    public SerializableInventory() {
        this.roomAvailability = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        roomAvailability.put(type, count);
    }

    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public void increaseAvailability(String type) {
        roomAvailability.put(type, getAvailability(type) + 1);
    }

    public void decreaseAvailability(String type) {
        roomAvailability.put(type, getAvailability(type) - 1);
    }

    public Map<String, Integer> getAllInventory() {
        return roomAvailability;
    }
}

// ---------- Persistence Service ----------
class PersistenceService {

    private static final String BOOKING_FILE = "bookings.dat";
    private static final String INVENTORY_FILE = "inventory.dat";

    // Save state to disk
    public static void saveState(List<SerializableBooking> bookings, SerializableInventory inventory) {
        try (ObjectOutputStream bookingOut = new ObjectOutputStream(new FileOutputStream(BOOKING_FILE));
             ObjectOutputStream inventoryOut = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {

            bookingOut.writeObject(bookings);
            inventoryOut.writeObject(inventory);

            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load booking history
    public static List<SerializableBooking> loadBookings() {
        File file = new File(BOOKING_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<SerializableBooking>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading booking history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Load inventory state
    public static SerializableInventory loadInventory() {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) return new SerializableInventory();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SerializableInventory) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
            return new SerializableInventory();
        }
    }
}

// ---------- Main Application ----------
public class BookMyStayPersistenceApp {

    public static void main(String[] args) {

        System.out.println("===== Booking System with Persistence & Recovery =====");

        // Recover previous state
        List<SerializableBooking> bookings = PersistenceService.loadBookings();
        SerializableInventory inventory = PersistenceService.loadInventory();

        // Display restored state
        System.out.println("\nRestored Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAllInventory().entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }

        System.out.println("\nRestored Bookings:");
        for (SerializableBooking b : bookings) {
            b.display();
        }

        // Simulate new booking
        SerializableBooking newBooking = new SerializableBooking("Alice", "Single Room", "SI-12345");
        bookings.add(newBooking);
        inventory.decreaseAvailability("Single Room");

        System.out.println("\nNew booking added:");
        newBooking.display();

        // Persist updated state
        PersistenceService.saveState(bookings, inventory);

        System.out.println("\nSystem shutdown complete. State persisted.");
    }
}

