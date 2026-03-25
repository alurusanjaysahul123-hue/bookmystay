import java.util.*;

// ---------- Booking Request ----------
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
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

// ---------- Request Queue Manager ----------
class RequestQueueManager {
    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    public BookingRequest getNextRequest() {
        return requestQueue.poll(); // FIFO removal
    }

    public boolean hasRequests() {
        return !requestQueue.isEmpty();
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

    public void reduceAvailability(String type) {
        int current = getAvailability(type);
        if (current > 0) {
            roomAvailability.put(type, current - 1);
        }
    }
}

// ---------- Allocation Service ----------
class AllocationService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    public void processBooking(BookingRequest request, InventoryService inventory) {

        String type = request.getRoomType();

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("No rooms available for " + type + " (Guest: " + request.getGuestName() + ")");
            return;
        }

        // Generate unique ID
        String roomId;
        do {
            roomId = generateRoomId(type);
        } while (allocatedRoomIds.contains(roomId));

        // Store ID in global set (ensures uniqueness)
        allocatedRoomIds.add(roomId);

        // Map room type -> allocated IDs
        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Update inventory immediately
        inventory.reduceAvailability(type);

        // Confirmation
        System.out.println("Booking Confirmed!");
        System.out.println("Guest: " + request.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Allocated Room ID: " + roomId);
        System.out.println("----------------------------------");
    }
}

// ---------- Main Application ----------
public class BookMyStayEngine {

    public static void main(String[] args) {

        System.out.println("===== Booking Allocation System =====");

        // Initialize services
        RequestQueueManager queueManager = new RequestQueueManager();
        InventoryService inventory = new InventoryService();
        AllocationService allocator = new AllocationService();

        // Setup inventory
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Add booking requests (FIFO)
        queueManager.addRequest(new BookingRequest("Alice", "Single Room"));
        queueManager.addRequest(new BookingRequest("Bob", "Single Room"));
        queueManager.addRequest(new BookingRequest("Charlie", "Single Room")); // should fail
        queueManager.addRequest(new BookingRequest("David", "Double Room"));

        // Process queue
        while (queueManager.hasRequests()) {
            BookingRequest request = queueManager.getNextRequest();
            allocator.processBooking(request, inventory);
        }

        System.out.println("\nAll requests processed.");
    }
}
