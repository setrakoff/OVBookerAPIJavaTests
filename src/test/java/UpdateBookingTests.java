import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTests extends MainTest {

    private final Booking updatedBooking = new Booking("Ivan", "Dorn", 4321, true,
            new BookingDates("2027-04-11","2027-04-22"), "Make sound max!");

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createNewBooking();
        apiClient.createToken(apiClient.getAdmin_username(), apiClient.getAdmin_password());
    }

    @Test
    public void testUpdateExistingBooking() {
        Response response = apiClient.updateBooking(createdBookingId, objectMapper.writeValueAsString(updatedBooking));
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        Booking respondedUpdatedBooking = objectMapper.readValue(responseBody, new TypeReference<Booking>() {});

        assertThat(respondedUpdatedBooking).isNotNull();
        assertThat(respondedUpdatedBooking.getFirstName()).isEqualTo(updatedBooking.getFirstName());
        assertThat(respondedUpdatedBooking.getLastName()).isEqualTo(updatedBooking.getLastName());
        assertThat(respondedUpdatedBooking.getLastName()).isEqualTo(updatedBooking.getLastName());
        assertThat(respondedUpdatedBooking.getTotalPrice()).isEqualTo(updatedBooking.getTotalPrice());
        assertThat(respondedUpdatedBooking.isDepositPaid()).isEqualTo(updatedBooking.isDepositPaid());
        assertThat(respondedUpdatedBooking.getAdditionalNeeds()).isEqualTo(updatedBooking.getAdditionalNeeds());

        assertThat(respondedUpdatedBooking.getBookingDates().getCheckin()).isEqualTo(updatedBooking.getBookingDates().getCheckin());
        assertThat(respondedUpdatedBooking.getBookingDates().getCheckout()).isEqualTo(updatedBooking.getBookingDates().getCheckout());


        response = apiClient.getBookingById(createdBookingId);
        assertThat(response.getStatusCode()).isEqualTo(200);

        responseBody = response.getBody().asString();
        respondedUpdatedBooking = objectMapper.readValue(responseBody, new TypeReference<Booking>() {});

        assertThat(respondedUpdatedBooking).isNotNull();
        assertThat(respondedUpdatedBooking.getFirstName()).isEqualTo(updatedBooking.getFirstName());
        assertThat(respondedUpdatedBooking.getLastName()).isEqualTo(updatedBooking.getLastName());
        assertThat(respondedUpdatedBooking.getLastName()).isEqualTo(updatedBooking.getLastName());
        assertThat(respondedUpdatedBooking.getTotalPrice()).isEqualTo(updatedBooking.getTotalPrice());
        assertThat(respondedUpdatedBooking.isDepositPaid()).isEqualTo(updatedBooking.isDepositPaid());
        assertThat(respondedUpdatedBooking.getAdditionalNeeds()).isEqualTo(updatedBooking.getAdditionalNeeds());

        assertThat(respondedUpdatedBooking.getBookingDates().getCheckin()).isEqualTo(updatedBooking.getBookingDates().getCheckin());
        assertThat(respondedUpdatedBooking.getBookingDates().getCheckout()).isEqualTo(updatedBooking.getBookingDates().getCheckout());

    }

    @AfterEach
    public void tearDown() {
        deleteCreatedBooking();
    }
}
