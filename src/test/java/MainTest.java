import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import core.models.BookingId;
import core.models.CreatedBooking;
import io.restassured.response.Response;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    protected APIClient apiClient;
    protected ObjectMapper objectMapper;
    protected int createdBookingId;
    protected final Booking newBooking = new Booking("Mark", "Ericsson", 1234, false,
            new BookingDates("2026-05-27","2026-06-17"), "Silence!");


    protected List<BookingId> getBookingIds() {
        return getBookingIds(apiClient.getBookingIds());
    }

    protected List<BookingId> getBookingIds(Response response) {
        String responseBody = response.getBody().asString();
        return objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});
    }

    protected void createNewBooking() {
        createdBookingId = createNewBooking(newBooking);
    }
    protected Integer createNewBooking(Booking booking) {
        Response response = apiClient.createBooking(objectMapper.writeValueAsString(booking));
        String responseBody = response.getBody().asString();
        CreatedBooking createdBooking = objectMapper.readValue(responseBody, new TypeReference<CreatedBooking>() {});
        return createdBooking.getBookingId();
    }

    protected void deleteCreatedBooking() {
        deleteBooking(createdBookingId);
    }

    protected void deleteBooking(int bookingId) {
        apiClient.createToken(apiClient.getAdmin_username(), apiClient.getAdmin_password());
        Response response = apiClient.deleteBookingById(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(201);
    }
}
