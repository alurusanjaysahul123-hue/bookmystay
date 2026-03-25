import java.util.*;

// ---------- Custom Exception for Invalid Booking ----------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------- Booking Request ----------
class BookingRequest1 {
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

// ---------- Inventory Service with Validation ----------
class InventoryService {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public void addRoomType(String type, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Room count cannot be negative.");
        }
        roomAvailability.put(type, count);
    }

    public int getAvailability(String type) throws InvalidBookingException {
        if (!roomAvailability.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
        return roomAvailability.get(type);
    }

    public void reduceAvailability(String type) throws InvalidBookingException {
        int available = getAvailability(type);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + type);
        }
        roomAvailability.put(type, available - 1);
    }
}

// ---------- Booking Validator ----------
class BookingValidator {

    public static void validateRequest(BookingRequest request, InventoryService inventory) throws InvalidBookingException {
        if (request.getGuestName() == null || request.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (request.getRoomType() == null || request.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        // Check inventory availability
        inventory.getAvailability(request.getRoomType());
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

    public void allocateRoom(BookingRequest request, InventoryService inventory) {
        try {
            // Validate booking request
            BookingValidator.validateRequest(request, inventory);

            // Reduce inventory
            inventory.reduceAvailability(request.getRoomType());

            // Generate unique room ID
            String roomId;
            do {
                roomId = generateRoomId(request.getRoomType());
            } while (allocatedRoomIds.contains(roomId));

            allocatedRoomIds.add(roomId);
            roomAllocations.putIfAbsent(request.getRoomType(), new HashSet<>());
            roomAllocations.get(request.getRoomType()).add(roomId);

            System.out.println("Booking Confirmed!");
            System.out.println("Guest: " + request.getGuestName());
            System.out.println("Room Type: " + request.getRoomType());
            System.out.println("Allocated Room ID: " + roomId);
            System.out.println("----------------------------------");

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
            System.out.println("----------------------------------");
        }
    }
}

// ---------- Main Application ----------
public class BookMyStayValidationApp {

    public static void main(String[] args) {

        System.out.println("===== Booking System with Error Handling =====");

        // Initialize inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0); // purposely zero to test error handling

        // Initialize allocation service
        AllocationService allocator = new AllocationService();

        // Test booking requests (including invalid cases)
        List<BookingRequest> requests = Arrays.asList(
                new BookingRequest("Alice", "Single Room"),       // valid
                new BookingRequest("", "Single Room"),            // invalid guest name
                new BookingRequest("Bob", "Double Room"),        // zero availability
                new BookingRequest("Charlie", "Suite Room")      // invalid room type
        );

        // Process all booking requests
        for (BookingRequest request : requests) {
            allocator.allocateRoom(request, inventory);
        }

        System.out.println("\nAll booking requests processed.");
    }
}