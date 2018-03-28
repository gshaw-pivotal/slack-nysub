package gs.nysub.components;

import gs.nysub.models.NoStatusInfoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NYSubwayStatusRequesterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NYSubwayStatusRequester nySubwayStatusRequester;

    private final String statusURL = "http://web.mta.info/status/serviceStatus.txt";

    private final String statusResponse = "<service><subway><line></line></subway></service>";

    @Before
    public void setup() {
    }

    @Test
    public void makeGetRequestToTheStatusPage() throws NoStatusInfoException {
        when(restTemplate.getForEntity(statusURL, String.class)).thenReturn(new ResponseEntity<String>(HttpStatus.OK));

        nySubwayStatusRequester.makeRequestForStatus();

        verify(restTemplate).getForEntity(statusURL, String.class);
    }

    @Test(expected = NoStatusInfoException.class)
    public void makeGetRequest_receiveStatusOtherThan200_throwException () throws NoStatusInfoException {
        when(restTemplate.getForEntity(statusURL, String.class)).thenReturn(new ResponseEntity<String>(HttpStatus.NOT_FOUND));

        nySubwayStatusRequester.makeRequestForStatus();

        verify(restTemplate).getForEntity(statusURL, String.class);
    }

    @Test
    public void makeGetRequest_receiveValidResponse_returnResponseBody() throws NoStatusInfoException {
        when(restTemplate.getForEntity(statusURL, String.class)).thenReturn(new ResponseEntity<String>(statusResponse, HttpStatus.OK));

        assertThat(nySubwayStatusRequester.makeRequestForStatus(), equalTo(statusResponse));

        verify(restTemplate).getForEntity(statusURL, String.class);
    }
}