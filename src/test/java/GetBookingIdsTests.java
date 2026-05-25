import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import core.models.BookingId;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTests extends MainTest {
    private Booking booking2 = new Booking("Marker", "Ericsson", 123456, false,
            new BookingDates("2026-05-28","2026-06-16"), "Silence! Or death!");
    private Booking booking3 = new Booking("Olga", "Belova", 55543, true,
            new BookingDates("2025-04-28","2026-06-17"), "I love flowers.");
    private Integer bookingId2;
    private Integer bookingId3;
    Map<String, String> filter;


    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        createNewBooking();
        bookingId2 = createNewBooking(booking2);
        bookingId3 = createNewBooking(booking3);
        filter = new HashMap<>();
    }

    @Test
    public void testGetBookingIds() {
        Response response = apiClient.getBookingIds();
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<BookingId> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});

        assertThat(bookingIds).isNotEmpty();

        for(BookingId bookingId : bookingIds) {
            assertThat(bookingId.getBookingId()).isGreaterThan(0);
        }
    }

    @Test
    public void testGetBookingIdsWithFilterFirstName() {
        filter.put("firstname", booking2.getFirstName());

        Response response = apiClient.getBookingIds(filter);
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<BookingId> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});

        assertThat(bookingIds).extracting(BookingId::getBookingId).contains(bookingId2);
    }

    @Test
    public void testGetBookingIdsWithFilterLastNameWithSeveralResults() {
        filter.put("lastname", booking2.getLastName());

        Response response = apiClient.getBookingIds(filter);
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<BookingId> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});

        assertThat(bookingIds).extracting(BookingId::getBookingId).contains(bookingId2, createdBookingId);
    }

    @Test
    public void testGetBookingIdsWithFilterBookingDatesOneResult() {
        filter.put("checkin", booking2.getBookingDates().getCheckin());
        filter.put("checkout", booking2.getBookingDates().getCheckout());

        Response response = apiClient.getBookingIds(filter);
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<BookingId> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingId>>() {});

        assertThat(bookingIds).extracting(BookingId::getBookingId).contains(bookingId2, createdBookingId);
    }


    @AfterEach
    public void tearDown() {
        deleteCreatedBooking();
        deleteBooking(bookingId2);
        deleteBooking(bookingId3);
    }

}
