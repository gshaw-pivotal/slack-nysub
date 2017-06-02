package gs.nysub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NYSubwayService {

    @Autowired
    private NYSubwayStatusRequestProcessor nySubwayStatusRequestProcessor;

    @Autowired
    private UptimeActuator uptimeActuator;

    @Autowired
    private SubwayLines subwayLines;

    public String processRequest(String userRequest) {
        if (userRequest.toLowerCase().contains("uptime")) {
            return "Current uptime is: " + formatUptime(uptimeActuator.getBotUptime());
        }
        if (userRequest.trim().toLowerCase().endsWith("status")) {
            return "The current status of all MTA lines are:\n" +
                    nySubwayStatusRequestProcessor.currentStatusAll().stream().collect(Collectors.joining());
        }
        if (userRequest.toLowerCase().contains("status") && !userRequest.trim().toLowerCase().endsWith("status")) {

            String[] parts = userRequest.toLowerCase().split("status");
            String[] lines = parts[1].trim().split("\\s+|,\\s?");

            String reply = "";
            for (int i = 0; i < lines.length; i++) {
                reply = reply + "\n" + reportOnLine(lines[i]);
            }
            return reply.trim();
        }
        if (userRequest.toLowerCase().contains("cassowary")) {
            return "Savage overgrown chook with pterodactyl style toes and a battering ram on top of its head";
        }
        if (userRequest.toLowerCase().contains("test")) {
            return "";
        }
        else {
            return instructions();
        }
    }

    private String reportOnLine(String line) {
        if (validLine(line.toUpperCase())) {
            return "The current status of *" + line.toUpperCase() + "* trains is: \n"
                    + nySubwayStatusRequestProcessor.currentStatus(line.toUpperCase());
        }
        else {
            return "Unknown train " + line.toUpperCase() + "\n";
        }
    }

    private boolean validLine(String line) {
        return subwayLines.isValidLine(line);
    }

    private String instructions() {
        return "Hi, I am nysub\n" +
                "You can utilise me like so:\n" +
                "'@nysub status' - get the status of all MTA subway lines\n" +
                "'@nysub status n' - get the status of the N line\n" +
                "'@nysub status n w' - get the status of the N & W lines\n" +
                "'@nysub uptime' - get my current uptime, since last restart";
    }

    private String formatUptime(long uptime) {
        long seconds = (uptime / 1000) % 60;
        long minutes = (uptime / 1000 / 60) % 60;
        long hours = (uptime / 1000 / 60 /60) % 24;
        long days = (uptime / 1000 / 60 /60 / 24);

        return String.format("%dd:%02dh:%02dm:%02ds", days, hours, minutes, seconds);
    }
}
