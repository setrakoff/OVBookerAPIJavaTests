import core.clients.APIClient;
import core.models.BookingId;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private List<BookingId> bookingIds;
    private BookingId bookingId;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken(apiClient.getAdmin_username(), apiClient.getAdmin_password());
    }

    @Test
    public void testDeleteExistingBooking() {
        bookingId = getBookingIds().getLast();

        Response response = apiClient.deleteBookingById(bookingId.getBookingId());
        assertThat(response.getStatusCode()).isEqualTo(201);

        bookingIds = getBookingIds();
        for(BookingId bookingId : bookingIds) {
            assertThat(bookingId.getBookingId()).isNotEqualTo(bookingId.getBookingId());
        }
    }

    @Test
    public void testDeleteUnexistingBooking() {
        int bookingId = 0;
        Response response = apiClient.getBookingById(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    private List<BookingId> getBookingIds() {
        Response response = apiClient.getBookingIds();
        String responseBody = response.getBody().asString();
        return objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});
    }
}
