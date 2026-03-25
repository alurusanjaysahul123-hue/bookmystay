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

// ---------- Booking History Store ----------
class BookingHistoryStore {
    private List<ConfirmedBooking> bookingList = new ArrayList<>();

    // Add confirmed booking (maintains order)
    public void addBooking(ConfirmedBooking booking) {
        bookingList.add(booking);
    }

    // Retrieve all bookings (read-only)
    public List<ConfirmedBooking> getAllBookings() {
        return bookingList;
    }
}

// ---------- Reporting Service ----------
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<ConfirmedBooking> bookings) {
        System.out.println("\n===== Booking History =====");

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (ConfirmedBooking booking : bookings) {
            booking.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<ConfirmedBooking> bookings) {

        System.out.println("\n===== Booking Summary Report =====");

        Map<String, Integer> summary = new HashMap<>();

        for (ConfirmedBooking booking : bookings) {
            String type = booking.getRoomType();
            summary.put(type, summary.getOrDefault(type, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + " -> Total Bookings: " + entry.getValue());
        }

        System.out.println("Total Confirmed Bookings: " + bookings.size());
    }
}

// ---------- Main Application ----------
public class BookMyStayReportApp {

    public static void main(String[] args) {

        System.out.println("===== Booking History & Reporting =====");

        // Initialize history store
        BookingHistoryStore historyStore = new BookingHistoryStore();

        // Simulate confirmed bookings (from allocation stage)
        historyStore.addBooking(new ConfirmedBooking("Alice", "Single Room", "SI-12345"));
        historyStore.addBooking(new ConfirmedBooking("Bob", "Double Room", "DO-54321"));
        historyStore.addBooking(new ConfirmedBooking("Charlie", "Single Room", "SI-67890"));

        // Reporting service
        BookingReportService reportService = new BookingReportService();

        // Display booking history
        reportService.showAllBookings(historyStore.getAllBookings());

        // Generate summary report
        reportService.generateSummary(historyStore.getAllBookings());

        System.out.println("\nReport generation completed.");
    }
}