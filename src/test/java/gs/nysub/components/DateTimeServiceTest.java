package gs.nysub.components;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class DateTimeServiceTest {

    private DateTimeService dateTimeService;

    @Before
    public void setup() {
        dateTimeService = new DateTimeService();
    }

    @Test
    public void requestForCurrentDateAndTime_returnsAListContainingTwoEntries() {
        List<String> dateTime = dateTimeService.getDateTime();

        assertThat(dateTime.size(), equalTo(2));
    }

    @Test
    public void requestForCurrentDateAndTime_returnsDateAndTimeInTheDesiredFormat() {
        String datePattern = "^\\d{2}-[a-zA-Z]{3}-\\d{4}$";
        String timePattern = "^\\d{2}:\\d{2}:\\d{2}$";

        List<String> dateTime = dateTimeService.getDateTime();

        assertTrue(dateTime.get(0).matches(datePattern));
        assertTrue(dateTime.get(1).matches(timePattern));
    }
}