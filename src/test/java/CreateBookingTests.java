import core.clients.APIClient;
import core.models.BookingId;
import core.models.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTests extends MainTest {

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateBooking() {
        Response response = apiClient.createBooking(objectMapper.writeValueAsString(newBooking));
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        CreatedBooking createdBooking = objectMapper.readValue(responseBody, new TypeReference<CreatedBooking>() {});
        createdBookingId = createdBooking.getBookingId();

        assertThat(createdBooking).isNotNull();

        assertThat(createdBooking.getBooking().getFirstName()).isEqualTo(newBooking.getFirstName());
        assertThat(createdBooking.getBooking().getLastName()).isEqualTo(newBooking.getLastName());
        assertThat(createdBooking.getBooking().getTotalPrice()).isEqualTo(newBooking.getTotalPrice());
        assertThat(createdBooking.getBooking().isDepositPaid()).isEqualTo(newBooking.isDepositPaid());
        assertThat(createdBooking.getBooking().getAdditionalNeeds()).isEqualTo(newBooking.getAdditionalNeeds());

        assertThat(createdBooking.getBooking().getBookingDates().getCheckin()).isEqualTo(newBooking.getBookingDates().getCheckin());
        assertThat(createdBooking.getBooking().getBookingDates().getCheckout()).isEqualTo(newBooking.getBookingDates().getCheckout());

        List<BookingId> bookingIds = getBookingIds();
        assertThat(bookingIds).extracting(BookingId::getBookingId).contains(createdBookingId);
    }

    @AfterEach
    public void tearDown() {
        deleteCreatedBooking();
    }
}
