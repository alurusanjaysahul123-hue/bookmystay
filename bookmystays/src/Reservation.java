import java.util.*;

// ---------- Reservation (Booking Request) ----------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ---------- Booking Request Queue ----------
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void showAllRequests() {
        System.out.println("\n===== Booking Requests Queue =====");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (FIFO)
    public Reservation getNextRequest() {
        return queue.peek();
    }
}

// ---------- Main Application ----------
public class BookMyStayMain {

    public static void main(String[] args) {

        System.out.println("===== Booking Request System (FCFS) =====");

        // Initialize Booking Queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate incoming requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display all queued requests
        bookingQueue.showAllRequests();

        // Show next request to be processed (FIFO)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.getNextRequest();

        if (next != null) {
            next.display();
        }

        System.out.println("\nNote: No rooms allocated yet. Inventory unchanged.");
    }
}