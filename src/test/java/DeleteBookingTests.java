import core.clients.APIClient;
import core.models.BookingId;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTests extends MainTest {

    private List<BookingId> bookingIds;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createNewBooking();
        apiClient.createToken(apiClient.getAdmin_username(), apiClient.getAdmin_password());
    }

    @Test
    public void testDeleteExistingBooking() {
        Response response = apiClient.deleteBookingById(createdBookingId);
        assertThat(response.getStatusCode()).isEqualTo(201);

        bookingIds = getBookingIds();
        for(BookingId bookingId : bookingIds) {
            assertThat(bookingId.getBookingId()).isNotEqualTo(createdBookingId);
        }
    }

    @Test
    public void testDeleteUnexistingBooking() {
        int bookingId = 0;
        Response response = apiClient.getBookingById(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(404);
    }
}
