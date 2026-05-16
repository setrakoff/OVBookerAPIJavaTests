import core.clients.APIClient;
import core.models.BookingId;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBookingIds() {
        Response response = apiClient.getBooking();
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<BookingId> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});

        assertThat(bookingIds).isNotEmpty();

        for(BookingId bookingId : bookingIds) {
            assertThat(bookingId.getBookingId()).isGreaterThan(0);
        }


    }

}
