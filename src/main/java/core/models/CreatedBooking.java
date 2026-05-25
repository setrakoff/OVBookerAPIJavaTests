package core.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatedBooking {
    private int bookingId;
    private Booking booking;

    @JsonCreator
    public CreatedBooking(@JsonProperty("bookingid") int bookingId,
                          @JsonProperty("booking") Booking booking) {
        this.bookingId = bookingId;
        this.booking = booking;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

}
