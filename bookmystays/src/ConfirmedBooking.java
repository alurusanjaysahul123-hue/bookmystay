import java.util.*;

// ---------- Confirmed Booking ----------
class ConfirmedBooking {
    private String guestName;
    private String roomType;
    private String roomId;

    public ConfirmedBooking(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void display() {
        System.out.println("Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// ---------- Inventory Service ----------
class InventoryService {
    private Map<String, Integer> roomAvailability = new HashMap<>();

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
}

// ---------- Booking History Store ----------
class BookingHistoryStore {
    private List<ConfirmedBooking> bookingList = new ArrayList<>();

    public void addBooking(ConfirmedBooking booking) {
        bookingList.add(booking);
    }

    public boolean removeBookingByRoomId(String roomId) {
        return bookingList.removeIf(b -> b.getRoomId().equals(roomId));
    }

    public boolean exists(String roomId) {
        return bookingList.stream().anyMatch(b -> b.getRoomId().equals(roomId));
    }

    public List<ConfirmedBooking> getAllBookings() {
        return bookingList;
    }
}

// ---------- Cancellation Service ----------
class CancellationService {
    private Stack<String> rollbackStack = new Stack<>();
    private InventoryService inventory;
    private BookingHistoryStore history;

    public CancellationService(InventoryService inventory, BookingHistoryStore history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String roomId, String roomType) {
        if (!history.exists(roomId)) {
            System.out.println("Cancellation Failed: No booking found with Room ID " + roomId);
            System.out.println("----------------------------------");
            return;
        }

        // Add to rollback stack (LIFO behavior)
        rollbackStack.push(roomId);

        // Remove from booking history
        history.removeBookingByRoomId(roomId);

        // Restore inventory immediately
        inventory.increaseAvailability(roomType);

        System.out.println("Booking Cancelled Successfully!");
        System.out.println("Room ID Released: " + roomId);
        System.out.println("Inventory Restored for Room Type: " + roomType);
        System.out.println("----------------------------------");
    }

    // Optional: View rollback stack
    public void showRollbackStack() {
        System.out.println("Rollback Stack (Most recent cancellations first): " + rollbackStack);
    }
}

// ---------- Main Application ----------
public class BookMyStayCancellationApp {

    public static void main(String[] args) {

        System.out.println("===== Booking Cancellation & Inventory Rollback =====");

        // Initialize services
        InventoryService inventory = new InventoryService();
        BookingHistoryStore historyStore = new BookingHistoryStore();
        CancellationService cancelService = new CancellationService(inventory, historyStore);

        // Sample confirmed bookings
        ConfirmedBooking b1 = new ConfirmedBooking("Alice", "Single Room", "SI-12345");
        ConfirmedBooking b2 = new ConfirmedBooking("Bob", "Double Room", "DO-54321");

        // Add bookings to history
        historyStore.addBooking(b1);
        historyStore.addBooking(b2);

        // Initialize inventory counts
        inventory.addRoomType("Single Room", 0);
        inventory.addRoomType("Double Room", 0);

        // Show initial bookings
        System.out.println("\nCurrent Bookings:");
        for (ConfirmedBooking b : historyStore.getAllBookings()) {
            b.display();
        }

        System.out.println("\nProcessing cancellations...\n");

        // Cancel bookings
        cancelService.cancelBooking("SI-12345", "Single Room");  // valid cancellation
        cancelService.cancelBooking("DO-54321", "Double Room");  // valid cancellation
        cancelService.cancelBooking("XX-99999", "Single Room");  // invalid cancellation

        // Show final bookings and inventory
        System.out.println("\nFinal Bookings:");
        for (ConfirmedBooking b : historyStore.getAllBookings()) {
            b.display();
        }

        System.out.println("\nFinal Inventory:");
        System.out.println("Single Room Available: " + inventory.getAvailability("Single Room"));
        System.out.println("Double Room Available: " + inventory.getAvailability("Double Room"));

        // Optional: Show rollback stack
        cancelService.showRollbackStack();
    }
}