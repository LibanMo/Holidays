public class BookingDetails {

    int id;
    int bookingId;
    int hotelId;
    int roomId;
    String startDate;
    String endDate;

    public BookingDetails(int id, int bookingId, int hotelId, int roomId, String startDate, String endDate) {
        this.id = id;
        this.bookingId = bookingId;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
