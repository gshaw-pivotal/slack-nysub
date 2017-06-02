package gs.nysub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NYSubwayStatusRequestProcessor {

    @Autowired
    private SubwayLines subwayLines;

    @Autowired
    private NYSubwayStatusRequester nySubwayStatusRequester;

    @Autowired
    private NYSubwayStatusExtractor nySubwayStatusExtractor;

    public void collectCurrentStatus() {
        try {
            nySubwayStatusExtractor.extractStatusInfo(nySubwayStatusRequester.makeRequestForStatus());
        } catch (NoStatusInfoException e) {
        }
    }

    public String currentStatus(String subwayLine) {
        collectCurrentStatus();
        return nySubwayStatusExtractor.getLineStatus(subwayLine);
    }

    public List<String> currentStatusAll() {
        collectCurrentStatus();

        List<String> statuses = new ArrayList<>();

        String[] lines = subwayLines.getSubwayLines();

        for (int index = 0; index < lines.length; index++) {
            statuses.add("*" + lines[index] + "*: " + currentStatus(lines[index]) + "\n------\n");
        }

        return statuses;
    }
}
