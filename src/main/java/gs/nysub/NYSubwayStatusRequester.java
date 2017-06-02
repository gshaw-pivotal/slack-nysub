package gs.nysub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NYSubwayStatusRequester {

    @Autowired
    private RestTemplate restTemplate;

    private final String statusURL = "http://web.mta.info/status/serviceStatus.txt";

    @Autowired
    public NYSubwayStatusRequester(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String makeRequestForStatus() throws NoStatusInfoException {
        ResponseEntity<String> response = restTemplate.getForEntity(statusURL, String.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new NoStatusInfoException();

        return response.getBody();
    }
}
