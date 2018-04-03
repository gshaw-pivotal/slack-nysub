package gs.nysub.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NYSubWebService {

    @Autowired
    private DateTimeService dateTimeService;

    public String generateStatus() {
        return "<HTML>" + generateRequestDateAndTime() + "</HTML>";
    }

    private String generateRequestDateAndTime() {
        List<String> dateTime = dateTimeService.getDateTime();
        return "Request Date: " + dateTime.get(0) + "\nRequest Time: " + dateTime.get(1);
    }
}
