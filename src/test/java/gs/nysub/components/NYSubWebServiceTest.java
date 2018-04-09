package gs.nysub.components;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NYSubWebServiceTest {

    private MockMvc mockMvc;

    @Mock
    private DateTimeService dateTimeService;

    @Mock
    private NYSubwayService nySubwayService;

    @InjectMocks
    private NYSubWebService nySubWebService;

    private String date = "01-Feb-2018";
    private String time = "11:22:33";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(nySubWebService).build();

        when(dateTimeService.getDateTime()).thenReturn(Arrays.asList(date, time));
    }

    @Test
    public void webServiceReturnsHTMLString() {
        String generatedResponse = nySubWebService.generateStatus();

        assertTrue(generatedResponse.startsWith("<HTML>"));
        assertTrue(generatedResponse.endsWith("</HTML>"));
    }

    @Test
    public void webServiceResponseIncludesDateAndTimeOfUserRequest() {
        String generatedResponse = nySubWebService.generateStatus();

        assertTrue(generatedResponse.contains("Request Date: " + date));
        assertTrue(generatedResponse.contains("Request Time: " + time));
    }

    @Test
    public void webServiceCallsTheDateService() {
        nySubWebService.generateStatus();

        verify(dateTimeService).getDateTime();
    }

    @Test
    public void webServiceCallsTheSubwayService() {
        nySubWebService.generateStatus();

        verify(nySubwayService).processWebRequest();
    }

    @Test
    public void webServiceReturnsFormattedLineStatus() {
        when(nySubwayService.processWebRequest()).thenReturn(generateSimpleSampleSubwayServiceResponse());

        String generatedResponse = nySubWebService.generateStatus();

        assertTrue(generatedResponse.contains("<span style=\"font-weight:bold\">1</span>"));
        assertTrue(generatedResponse.contains("<span style=\"font-weight:bold\">GOOD SERVICE</span>"));

        assertTrue(generatedResponse.contains("<span style=\"font-weight:bold\">2</span>"));
        assertTrue(generatedResponse.contains("<span style=\"font-weight:bold\">GOOD SERVICE</span>"));

        assertTrue(generatedResponse.contains("<span style=\"font-weight:bold\">3</span>"));
        assertTrue(generatedResponse.contains("<span style=\"font-weight:bold\">GOOD SERVICE</span>"));
    }

    private List<String> generateSimpleSampleSubwayServiceResponse() {
        return Arrays.asList(
                "*1*: *GOOD SERVICE*\n------\n",
                "*2*: *GOOD SERVICE*\n------\n",
                "*3*: *GOOD SERVICE*\n------\n"
        );
    }
}
