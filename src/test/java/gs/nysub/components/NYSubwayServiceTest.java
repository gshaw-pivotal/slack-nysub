package gs.nysub.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NYSubwayServiceTest {

    @Mock
    private UptimeActuator uptimeActuator;

    @Mock
    private SubwayLines subwayLines;

    @Mock
    private NYSubwayStatusRequestProcessor nySubwayStatusRequestProcessor;

    @InjectMocks
    private NYSubwayService nySubwayService;

    private List<String> statuses = Arrays.asList("A: Good Service\n", "B: Good Service\n");

    @Before
    public void setup() {
        when(subwayLines.isValidLine("N")).thenReturn(true);
        when(subwayLines.isValidLine("W")).thenReturn(true);
        when(subwayLines.isValidLine("X")).thenReturn(false);

        when(nySubwayStatusRequestProcessor.currentStatus("N")).thenReturn("Good Service");
        when(nySubwayStatusRequestProcessor.currentStatus("W")).thenReturn("Good Service");

        when(nySubwayStatusRequestProcessor.currentStatusAll()).thenReturn(statuses);
    }

    @Test
    public void serviceReceiveWebBasedStatusRequest_respondsWithAListOfAllLinesAndTheirStatus() {
        List<String> response = nySubwayService.processWebRequest();

        assertThat(response, equalTo(statuses));

        verify(nySubwayStatusRequestProcessor).currentStatusAll();
    }

    @Test
    public void serviceReceiveStatusRequest_respondWithStatusOfAllLines() {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status");

        assertEquals(response, "The current status of all MTA lines are:\nA: Good Service\nB: Good Service\n");

        verify(nySubwayStatusRequestProcessor).currentStatusAll();
    }

    @Test
    public void serviceReceiveSingleLineStatusRequest_respondWithOneLineStatus() throws Exception {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status N");

        assertEquals(response, "The current status of *N* trains is: \nGood Service");

        verify(nySubwayStatusRequestProcessor).currentStatus("N");
    }

    @Test
    public void serviceReceiveUnknownLineStatusRequest_respondWithLineNotFound() throws Exception {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status X");
        assertEquals(response, "Unknown train X");
    }

    @Test
    public void serviceReceiveMultiLineSpaceSepStatusRequest_respondWithMultiLineStatus() throws Exception {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status N W");

        assertEquals(response, "The current status of *N* trains is: \nGood Service\nThe current status of *W* trains is: \nGood Service");

        verify(nySubwayStatusRequestProcessor).currentStatus("N");
        verify(nySubwayStatusRequestProcessor).currentStatus("W");
    }

    @Test
    public void serviceReceiveMultiLineWithValidAndInvalidStatusRequest_respondWithMultiLineStatus() throws Exception {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status N X");

        assertEquals(response, "The current status of *N* trains is: \nGood Service\nUnknown train X");

        verify(nySubwayStatusRequestProcessor).currentStatus("N");
    }

    @Test
    public void serviceReceiveMultiLineCommaSepStatusRequest_respondWithMultiLineStatus() throws Exception {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status N,W");

        assertEquals(response, "The current status of *N* trains is: \nGood Service\nThe current status of *W* trains is: \nGood Service");


        verify(nySubwayStatusRequestProcessor).currentStatus("N");
        verify(nySubwayStatusRequestProcessor).currentStatus("W");
    }

    @Test
    public void serviceReceiveMultiLineSpaceCommaSepStatusRequest_respondWithMultiLineStatus() throws Exception {
        String response = nySubwayService.processRequest("<@UEADGH12S>: status N, W");

        assertEquals(response, "The current status of *N* trains is: \nGood Service\nThe current status of *W* trains is: \nGood Service");

        verify(nySubwayStatusRequestProcessor).currentStatus("N");
        verify(nySubwayStatusRequestProcessor).currentStatus("W");
    }

    @Test
    public void serviceReceivesEmptyRequest_respondWithInstructions() {
        String response = nySubwayService.processRequest("<@UEADGH12S>:");
        assertThat(response, containsString("Hi, I am nysub"));
        assertThat(response, containsString("You can utilise me like so:"));
    }

    @Test
    public void serviceReceivesUptimeRequest_respondsWithCurrentUptime() {
        when(uptimeActuator.getBotUptime()).thenReturn((long)200);

        String response = nySubwayService.processRequest("<@UEADGH12S>: Uptime");
        assertThat(response, containsString("Current uptime is: "));

        verify(uptimeActuator).getBotUptime();
    }

    //Test uptime in seconds
    @Test
    public void serviceReceivesUptimeRequest_respondsWithCurrentUptimeInSeconds() {
        when(uptimeActuator.getBotUptime()).thenReturn((long)30000);

        String response = nySubwayService.processRequest("<@UEADGH12S>: Uptime");
        assertThat(response, containsString("Current uptime is: 0d:00h:00m:30s"));

        verify(uptimeActuator).getBotUptime();
    }

    //Test uptime in minutes / seconds
    @Test
    public void serviceReceivesUptimeRequest_respondsWithCurrentUptimeInMinutes() {
        when(uptimeActuator.getBotUptime()).thenReturn((long)630000);

        String response = nySubwayService.processRequest("<@UEADGH12S>: Uptime");
        assertThat(response, containsString("Current uptime is: 0d:00h:10m:30s"));

        verify(uptimeActuator).getBotUptime();
    }

    @Test
    public void serviceReceivesUptimeRequest_respondsWithCurrentUptimeInHours() {
        when(uptimeActuator.getBotUptime()).thenReturn((long)6305000);

        String response = nySubwayService.processRequest("<@UEADGH12S>: Uptime");
        assertThat(response, containsString("Current uptime is: 0d:01h:45m:05s"));

        verify(uptimeActuator).getBotUptime();
    }

    @Test
    public void serviceReceivesUptimeRequest_respondsWithCurrentUptimeInDays() {
        when(uptimeActuator.getBotUptime()).thenReturn((long)179105000);

        String response = nySubwayService.processRequest("<@UEADGH12S>: Uptime");
        assertThat(response, containsString("Current uptime is: 2d:01h:45m:05s"));

        verify(uptimeActuator).getBotUptime();
    }
}