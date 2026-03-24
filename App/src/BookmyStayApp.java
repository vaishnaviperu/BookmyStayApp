import java.util.ArrayList;
import java.util.List;
class Reservation {
    private String guestName;
    private String roomType;
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType  = roomType;
    }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}
class BookingHistory {
    private List<Reservation> confirmedReservations;
    public BookingHistory() { confirmedReservations = new ArrayList<>(); }
    public void addReservation(Reservation reservation) { confirmedReservations.add(reservation); }
    public List<Reservation> getConfirmedReservations() { return confirmedReservations; }
}
class BookingReportService {
    public void generateReport(BookingHistory history) {
        System.out.println("Booking History Report");
        List<Reservation> reservations = history.getConfirmedReservations();
        for (Reservation r : reservations) {
            System.out.println("Guest: " + r.getGuestName() + ", Room Type: " + r.getRoomType());
        }
    }
}
public class BookmyStayApp{
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Abhi",     "Single");
        Reservation r2 = new Reservation("Subha",    "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        BookingReportService reportService = new BookingReportService();

        System.out.println("Booking History and Reporting");
        System.out.println();
        reportService.generateReport(history);
    }
}