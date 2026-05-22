import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

public class PartialUpdateBookingTests extends MainTest {

    private final Booking updateBooking = new Booking("Ivan", "Dorn", 4321, true,
            new BookingDates("2027-04-11","2027-04-22"), "Make sound max!");
    private Booking partialUpdatedBooking = new Booking(newBooking);

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createNewBooking();
        apiClient.createToken(apiClient.getAdmin_username(), apiClient.getAdmin_password());
    }

    @Test
    public void testPartialUpdateBooking() {
        partialUpdatedBooking.setFirstName(updateBooking.getFirstName());
        partialUpdatedBooking.setLastName(updateBooking.getLastName());
    }
}
