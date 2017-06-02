package gs.nysub;

import me.ramswaroop.jbot.core.slack.SlackService;
import me.ramswaroop.jbot.core.slack.models.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NYSubBotTest {

    @Mock
    private WebSocketSession session;

    @Mock
    private SlackService slackService;

    @Mock
    private NYSubwayService nySubwayService;

    @InjectMocks
    private NYSubBot bot;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Before
    public void init() {
        // set user
        User user = new User();
        user.setName("nysub");
        user.setId("UEADGH12S");
        // set rtm
        when(slackService.getDmChannels()).thenReturn(Arrays.asList("D1E79BACV", "C0NDSV5B8"));
        when(slackService.getCurrentUser()).thenReturn(user);
        when(slackService.getWebSocketUrl()).thenReturn("");
    }

    @Test
    public void when_DirectMention_WithNoMessageContent_Should_RespondWithIntro() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>:")).thenReturn("Hi, I am nysub\nYou can utilise me like so:");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>:\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("Hi, I am nysub"));
        assertThat(response, containsString("You can utilise me like so:"));

        verify(nySubwayService).processRequest("<@UEADGH12S>:");
    }

    @Test
    public void when_DirectMention_WithUptimeMessage_Should_RespondWithCurrentUptime() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Uptime")).thenReturn("Current uptime is: 0d:00h:00m:00s");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Uptime\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("Current uptime is: "));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Uptime");
    }

    @Test
    public void when_DirectMention_WithStatusMessage_Should_RespondWithAllLineStatus() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Status")).thenReturn("The current status of all MTA lines are:");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Status\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("The current status of all MTA lines are:"));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Status");
    }

    @Test
    public void when_DirectMention_WithStatusOneLineMessage_Should_RespondWithOneLineStatus() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Status N")).thenReturn("The current status of N trains is: ");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Status N\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("The current status of N trains is: "));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Status N");
    }

    @Test
    public void when_DirectMention_WithStatusNonExistentLineMessage_Should_RespondLineNotFound() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Status X")).thenReturn("Unknown train X");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Status X\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("Unknown train X"));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Status X");
    }

    @Test
    public void when_DirectMention_WithStatusMultiLineMessageSpaceSep_Should_RespondWithMultiLineStatus() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Status N W")).thenReturn("The current status of N trains is: \nThe current status of W trains is: ");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Status N W\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("The current status of N trains is: "));
        assertThat(response, containsString("The current status of W trains is: "));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Status N W");
    }

    @Test
    public void when_DirectMention_WithStatusMultiLineMessageCommaSep_Should_RespondWithMultiLineStatus() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Status N,W")).thenReturn("The current status of N trains is: \nThe current status of W trains is: ");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Status N,W\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("The current status of N trains is: "));
        assertThat(response, containsString("The current status of W trains is: "));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Status N,W");
    }

    @Test
    public void when_DirectMention_WithStatusMultiLineMessageSpaceCommaSep_Should_RespondWithMultiLineStatus() throws Exception {
        when(nySubwayService.processRequest("<@UEADGH12S>: Status N, W")).thenReturn("The current status of N trains is: \nThe current status of W trains is: ");

        TextMessage textMessage = new TextMessage("{\"type\": \"message\"," +
                "\"ts\": \"1358878749.000002\"," +
                "\"user\": \"U023BECGF\"," +
                "\"text\": \"<@UEADGH12S>: Status N, W\"}");
        bot.handleTextMessage(session, textMessage);

        String response = capture.toString();
        assertThat(response, containsString("The current status of N trains is: "));
        assertThat(response, containsString("The current status of W trains is: "));
        assertThat(response, not(containsString("The current status of  trains is: ")));

        verify(nySubwayService).processRequest("<@UEADGH12S>: Status N, W");
    }
}