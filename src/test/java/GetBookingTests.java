import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTests extends MainTest {

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createNewBooking();
    }

    @Test
    public void testGetExistingBooking() {
        Response response = apiClient.getBookingById(createdBookingId);
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        Booking booking = objectMapper.readValue(responseBody, new TypeReference<Booking>() {});

        assertThat(booking).isNotNull();

        assertThat(booking.getFirstName()).isEqualTo(newBooking.getFirstName());
        assertThat(booking.getLastName()).isEqualTo(newBooking.getLastName());
        assertThat(booking.getTotalPrice()).isEqualTo(newBooking.getTotalPrice());
        assertThat(booking.isDepositPaid()).isEqualTo(newBooking.isDepositPaid());
        assertThat(booking.getAdditionalNeeds()).isEqualTo(newBooking.getAdditionalNeeds());

        assertThat(booking.getBookingDates().getCheckin()).isEqualTo(newBooking.getBookingDates().getCheckin());
        assertThat(booking.getBookingDates().getCheckout()).isEqualTo(newBooking.getBookingDates().getCheckout());
    }

    @Test
    public void testGetUnexistingBooking() {
        int bookingId = 0;
        Response response = apiClient.getBookingById(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @AfterEach
    public void tearDown() {
        deleteCreatedBooking();
    }
}
