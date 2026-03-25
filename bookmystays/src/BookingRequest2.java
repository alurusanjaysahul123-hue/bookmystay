import java.util.*;
import java.util.concurrent.*;

// ---------- Booking Request ----------
class BookingRequest2 {
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

// ---------- Thread-Safe Inventory Service ----------
class ThreadSafeInventoryService {
    private final Map<String, Integer> roomAvailability = new HashMap<>();

    // Initialize room types
    public synchronized void addRoomType(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Thread-safe retrieval
    public synchronized int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    // Thread-safe decrement
    public synchronized boolean allocateRoom(String type) {
        int available = getAvailability(type);
        if (available > 0) {
            roomAvailability.put(type, available - 1);
            return true;
        }
        return false;
    }
}

// ---------- Thread-Safe Allocation Service ----------
class ConcurrentAllocationService {
    private final ThreadSafeInventoryService inventory;
    private final Set<String> allocatedRoomIds = Collections.synchronizedSet(new HashSet<>());

    public ConcurrentAllocationService(ThreadSafeInventoryService inventory) {
        this.inventory = inventory;
    }

    // Critical section: allocation per booking
    public void processBooking(BookingRequest request) {
        synchronized (inventory) {
            boolean success = inventory.allocateRoom(request.getRoomType());

            if (!success) {
                System.out.println("Booking Failed for " + request.getGuestName() +
                        " (" + request.getRoomType() + ") - No rooms available");
                return;
            }

            // Generate unique room ID (synchronized to avoid collisions)
            String roomId;
            do {
                roomId = request.getRoomType().substring(0, 2).toUpperCase() +
                        "-" + UUID.randomUUID().toString().substring(0, 5);
            } while (allocatedRoomIds.contains(roomId));

            allocatedRoomIds.add(roomId);

            System.out.println("Booking Confirmed for " + request.getGuestName() +
                    " | Room Type: " + request.getRoomType() +
                    " | Room ID: " + roomId);
        }
    }
}

// ---------- Runnable Task for Threads ----------
class BookingTask implements Runnable {
    private final BookingRequest request;
    private final ConcurrentAllocationService allocator;

    public BookingTask(BookingRequest request, ConcurrentAllocationService allocator) {
        this.request = request;
        this.allocator = allocator;
    }

    @Override
    public void run() {
        allocator.processBooking(request);
    }
}

// ---------- Main Application ----------
public class BookMyStayConcurrentApp {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("===== Concurrent Booking Simulation =====");

        // Initialize thread-safe inventory
        ThreadSafeInventoryService inventory = new ThreadSafeInventoryService();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Initialize allocation service
        ConcurrentAllocationService allocator = new ConcurrentAllocationService(inventory);

        // Create booking requests (simulate multiple guests)
        List<BookingRequest> requests = Arrays.asList(
                new BookingRequest("Alice", "Single Room"),
                new BookingRequest("Bob", "Single Room"),
                new BookingRequest("Charlie", "Single Room"), // should fail
                new BookingRequest("David", "Double Room"),
                new BookingRequest("Eve", "Double Room")      // should fail
        );

        // Executor service for concurrent processing
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (BookingRequest r : requests) {
            executor.execute(new BookingTask(r, allocator));
        }

        // Shutdown executor and wait for all tasks to finish
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nConcurrent booking simulation completed.");
    }
}