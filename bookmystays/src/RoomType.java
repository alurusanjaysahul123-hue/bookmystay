import java.util.*;

// ---------- Abstract Room ----------
abstract class RoomType {
    private String name;
    private int beds;
    private double price;

    public RoomType(String name, int beds, double price) {
        this.name = name;
        this.beds = beds;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract void showDetails();
}

// ---------- Concrete Room Types ----------
class SingleRoomType extends RoomType {
    public SingleRoomType() {
        super("Single Room", 1, 2000);
    }

    public void showDetails() {
        System.out.println("Type: " + getName() + ", Beds: " + getBeds() + ", Price: ₹" + getPrice());
    }
}

class DoubleRoomType extends RoomType {
    public DoubleRoomType() {
        super("Double Room", 2, 3500);
    }

    public void showDetails() {
        System.out.println("Type: " + getName() + ", Beds: " + getBeds() + ", Price: ₹" + getPrice());
    }
}

class SuiteRoomType extends RoomType {
    public SuiteRoomType() {
        super("Suite Room", 3, 6000);
    }

    public void showDetails() {
        System.out.println("Type: " + getName() + ", Beds: " + getBeds() + ", Price: ₹" + getPrice());
    }
}

// ---------- Inventory Manager ----------
class InventoryManager {
    private Map<String, Integer> roomStock = new HashMap<>();

    public void addRoom(String type, int count) {
        roomStock.put(type, count);
    }

    public int checkAvailability(String type) {
        return roomStock.getOrDefault(type, 0);
    }
}

// ---------- Room Search Service ----------
class RoomSearchService {

    public void displayAvailableRooms(InventoryManager inventory, List<RoomType> roomTypes) {

        System.out.println("\n===== Available Rooms =====");

        for (RoomType room : roomTypes) {

            int available = inventory.checkAvailability(room.getName());

            if (available > 0) {
                room.showDetails();
                System.out.println("Available: " + available);
                System.out.println("--------------------------");
            }
        }
    }
}

// ---------- Main Application ----------
public class BookMyStayMain {

    public static void main(String[] args) {

        System.out.println("===== Welcome to Book My Stay =====");

        // Initialize Inventory
        InventoryManager inventory = new InventoryManager();
        inventory.addRoom("Single Room", 5);
        inventory.addRoom("Double Room", 0);
        inventory.addRoom("Suite Room", 2);

        // Room Types
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(new SingleRoomType());
        roomTypes.add(new DoubleRoomType());
        roomTypes.add(new SuiteRoomType());

        // Search Service
        RoomSearchService searchService = new RoomSearchService();

        // Perform Search
        searchService.displayAvailableRooms(inventory, roomTypes);

        System.out.println("\nSearch completed. Inventory unchanged.");
    }
}