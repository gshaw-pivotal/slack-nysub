package gs.nysub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NYSubwayStatusRequestProcessorTest {

    @Mock
    private SubwayLines subwayLines;

    @Mock
    private NYSubwayStatusRequester nySubwayStatusRequester;

    @Mock
    private NYSubwayStatusExtractor nySubwayStatusExtractor;

    @InjectMocks
    private NYSubwayStatusRequestProcessor nySubwayStatusRequestProcessor;

    private String sampleMTAStatusString =
            "<service>" +
                    "<responsecode>0</responsecode>" +
                    "<timestamp>3/11/2017 1:36:00 AM</timestamp>" +
                    "<subway>" +
                    "<line>" +
                    "<name>L</name>" +
                    "<status>GOOD SERVICE</status>" +
                    "<text />" +
                    "<Date></Date>" +
                    "<Time></Time>" +
                    "</line>" +
                    "</subway>" +
                    "</service>";

    @Before
    public void setup() {
        when(subwayLines.getSubwayLines()).thenReturn(new String[]{"1", "2", "3", "4", "5", "6", "7", "A", "B", "C", "D", "E", "F", "G", "J", "L", "M", "N", "Q", "R", "S", "SIR", "W", "Z"});
    }

    @Test
    public void instructedToGetCurrentStatus_callsOutForCurrentStatus() throws NoStatusInfoException {
        when(nySubwayStatusRequester.makeRequestForStatus()).thenReturn(sampleMTAStatusString);

        nySubwayStatusRequestProcessor.collectCurrentStatus();

        verify(nySubwayStatusRequester).makeRequestForStatus();

        verify(nySubwayStatusExtractor).extractStatusInfo(sampleMTAStatusString);
    }

    @Test
    public void requestCurrentStatusOfSingleLine_returnsStatusOfThatLine_whenNoServiceIssues() {
        when(nySubwayStatusExtractor.getLineStatus("N")).thenReturn("Good Service");

        String lineStatus = nySubwayStatusRequestProcessor.currentStatus("N");

        assertThat(lineStatus, equalTo("Good Service"));

        verify(nySubwayStatusExtractor).getLineStatus("N");
    }

    @Test
    public void requestCurrentStatusOfAllLines_returnsStatusOfAllLines() {
        List<String> lineStatuses = nySubwayStatusRequestProcessor.currentStatusAll();

        assertThat(lineStatuses.size(), equalTo(24));

        verify(nySubwayStatusExtractor, times(24)).getLineStatus(anyString());
    }
}