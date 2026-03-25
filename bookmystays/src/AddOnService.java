import java.util.*;

// ---------- Add-On Service ----------
class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println(serviceName + " (₹" + price + ")");
    }
}

// ---------- Reservation Reference ----------
class ReservationReference {
    private String reservationId;
    private String guestName;

    public ReservationReference(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }
}

// ---------- Add-On Service Manager ----------
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Attach services to reservation
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).addAll(services);
    }

    // Retrieve services for reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0.0;
        for (AddOnService s : services) {
            total += s.getPrice();
        }
        return total;
    }

    // Display attached services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected for reservation " + reservationId);
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        for (AddOnService s : services) {
            s.display();
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// ---------- Main Application ----------
public class BookMyStayAddOnApp {

    public static void main(String[] args) {

        System.out.println("===== Add-On Service Selection =====");

        // Initialize Add-On Manager
        AddOnServiceManager addOnManager = new AddOnServiceManager();

        // Example reservations
        ReservationReference reservation1 = new ReservationReference("SI-12345", "Alice");
        ReservationReference reservation2 = new ReservationReference("DO-54321", "Bob");

        // Define available add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa Session", 1500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 700);

        // Attach services to reservation 1
        addOnManager.addServices(reservation1.getReservationId(), Arrays.asList(breakfast, spa));

        // Attach services to reservation 2
        addOnManager.addServices(reservation2.getReservationId(), Collections.singletonList(airportPickup));

        // Display services and total cost for each reservation
        addOnManager.displayServices(reservation1.getReservationId());
        System.out.println();
        addOnManager.displayServices(reservation2.getReservationId());
    }
}