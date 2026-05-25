import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        Map<String, String> updatePayload = new HashMap<>();
        updatePayload.put("firstname", updateBooking.getFirstName());
        updatePayload.put("lastname", updateBooking.getLastName());

        Response response = apiClient.partialUpdateBooking(createdBookingId, objectMapper.writeValueAsString(updatePayload));
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        Booking respondedBooking = objectMapper.readValue(responseBody, new TypeReference<Booking>() {});


        assertThat(respondedBooking).isNotNull();
        assertThat(respondedBooking.getFirstName()).isEqualTo(partialUpdatedBooking.getFirstName());
        assertThat(respondedBooking.getLastName()).isEqualTo(partialUpdatedBooking.getLastName());
        assertThat(respondedBooking.getLastName()).isEqualTo(partialUpdatedBooking.getLastName());
        assertThat(respondedBooking.getTotalPrice()).isEqualTo(partialUpdatedBooking.getTotalPrice());
        assertThat(respondedBooking.isDepositPaid()).isEqualTo(partialUpdatedBooking.isDepositPaid());
        assertThat(respondedBooking.getAdditionalNeeds()).isEqualTo(partialUpdatedBooking.getAdditionalNeeds());

        assertThat(respondedBooking.getBookingDates().getCheckin()).isEqualTo(partialUpdatedBooking.getBookingDates().getCheckin());
        assertThat(respondedBooking.getBookingDates().getCheckout()).isEqualTo(partialUpdatedBooking.getBookingDates().getCheckout());


        response = apiClient.getBookingById(createdBookingId);
        assertThat(response.getStatusCode()).isEqualTo(200);

        responseBody = response.getBody().asString();
        respondedBooking = objectMapper.readValue(responseBody, new TypeReference<Booking>() {});

        assertThat(respondedBooking).isNotNull();
        assertThat(respondedBooking.getFirstName()).isEqualTo(partialUpdatedBooking.getFirstName());
        assertThat(respondedBooking.getLastName()).isEqualTo(partialUpdatedBooking.getLastName());
        assertThat(respondedBooking.getLastName()).isEqualTo(partialUpdatedBooking.getLastName());
        assertThat(respondedBooking.getTotalPrice()).isEqualTo(partialUpdatedBooking.getTotalPrice());
        assertThat(respondedBooking.isDepositPaid()).isEqualTo(partialUpdatedBooking.isDepositPaid());
        assertThat(respondedBooking.getAdditionalNeeds()).isEqualTo(partialUpdatedBooking.getAdditionalNeeds());

        assertThat(respondedBooking.getBookingDates().getCheckin()).isEqualTo(partialUpdatedBooking.getBookingDates().getCheckin());
        assertThat(respondedBooking.getBookingDates().getCheckout()).isEqualTo(partialUpdatedBooking.getBookingDates().getCheckout());
    }

    @AfterEach
    public void tearDown() {
        deleteCreatedBooking();
    }
}
