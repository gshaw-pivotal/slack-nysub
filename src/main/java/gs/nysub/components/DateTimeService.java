package gs.nysub.components;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class DateTimeService {

    public List<String> getDateTime() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-YYYY"));
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return Arrays.asList(currentDate, currentTime);
    }
}
