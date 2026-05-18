import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTests {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private final int bookingId = 2;
    private final Booking expectedBooking = new Booking("Mark", "Ericsson", 837, true,
            new BookingDates("2023-04-27","2025-07-17"), null);

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetExistingBooking() {
        Response response = apiClient.getBookingById(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        Booking booking = objectMapper.readValue(responseBody, new TypeReference<Booking>() {});

        assertThat(booking).isNotNull();

        assertThat(booking.getFirstName()).isEqualTo(expectedBooking.getFirstName());
        assertThat(booking.getLastName()).isEqualTo(expectedBooking.getLastName());
        assertThat(booking.getTotalPrice()).isEqualTo(expectedBooking.getTotalPrice());
        assertThat(booking.isDepositPaid()).isEqualTo(expectedBooking.isDepositPaid());
        assertThat(booking.getAdditionalNeeds()).isEqualTo(expectedBooking.getAdditionalNeeds());

        assertThat(booking.getBookingDates().getCheckin()).isEqualTo(expectedBooking.getBookingDates().getCheckin());
        assertThat(booking.getBookingDates().getCheckout()).isEqualTo(expectedBooking.getBookingDates().getCheckout());
    }

    @Test
    public void testGetUnexistingBooking() {
        int bookingId = 0;
        Response response = apiClient.getBookingById(bookingId);
        assertThat(response.getStatusCode()).isEqualTo(404);
    }
}
