package gs.nysub.components;

import gs.nysub.models.SubwayLineInfo;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NYSubwayStatusExtractor {

    private SubwayLineInfo subwayLineInfo;

    private Map<String, SubwayLineInfo> subwayLines;

    public NYSubwayStatusExtractor() {
        subwayLines = new HashMap<String, SubwayLineInfo>();
    }

    public void extractStatusInfo(String textStatus) {
        extractSubwayStatus(textStatus);
    }

    public String getLineStatus(String line) {
        if (subwayLines.containsKey(line)) {
            subwayLineInfo = subwayLines.get(line);
            return ("*"+subwayLineInfo.getStatus() + "* " +
                    subwayLineInfo.getStatusDate() + " " +
                    subwayLineInfo.getStatusTime() + "\n" +
                    subwayLineInfo.getStatusMessage()).trim();
        }

        return "Line not found";
    }

    private void extractSubwayStatus(String text) {
        String subway = extractSubwaySection(text);
        String[] lineGroups = extractSubwayLineGroups(subway);

        Arrays.stream(lineGroups)
                .forEach(lg -> extractLineInfo(lg.replaceAll("<line>", "").trim()));
    }

    private String extractSubwaySection(String text) {
        int start = text.indexOf("<subway>");
        int end = text.indexOf("</subway>");

        if (start > -1 && (end > (start + 8))) {
            return text.substring(start + 8, end).trim();
        }

        return null;
    }

    private String[] extractSubwayLineGroups(String text) {
        return text.split("</line>\\s+<line>");
    }

    private void extractLineInfo(String lineGroup) {
        String lineID = extractPattern(lineGroup, "<name>(.*?)</name>");

        if (lineID.length() > 1 && !lineID.equals("SIR")) {
            for (int i = 0; i < lineID.length(); i++) {
                buildSubwayLineInfo(String.valueOf(lineID.charAt(i)), lineGroup);
                subwayLines.put(String.valueOf(lineID.charAt(i)), subwayLineInfo);
            }

            if (lineID.equals("NQR")) {
                buildSubwayLineInfo(String.valueOf("W"), lineGroup);
                subwayLines.put(String.valueOf("W"), subwayLineInfo);
            }
        }
        else {
            buildSubwayLineInfo(lineID, lineGroup);
            subwayLines.put(lineID, subwayLineInfo);
        }
    }

    private String extractPattern(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private String extractTextSegment(String text) {
        int start = text.indexOf("<text>");
        int end = text.indexOf("</text>");

        if (start > -1 && (end > (start + 6))) {
            return text.substring(start + 6, end).trim();
        }

        return "";
    }

    private String cleanupTextSegment(String text) {
        text = text.replaceAll("\\r", "")
                .replaceAll("\\n", "")
                .replaceAll("&amp;", " ")
                .replaceAll("nbsp;", " ")
                .replaceAll("bull;", " ")
                .replaceAll("\\[TP\\]", " ")
                .replaceAll("\\[ad\\]", " ")
                .replaceAll("Â", "");

        int start = text.indexOf("&lt;");
        int end;

        while (start >= 0) {
            end = text.indexOf("&gt;", start);
            text = text.substring(0, start) + " " + text.substring(end + 4);

            start = text.indexOf("&lt;");
        }

        return text.replaceAll("\\s{2,}", " ").trim();
    }

    private String checkStatusAppliesToLine(String line, String status) {
        if (status.indexOf("[" + line + "]") >= 0 || status.length() == 0) return status;

        String lineList = "";
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(status);
        while (matcher.find()) {
            if (lineList.indexOf(matcher.group(1)) == -1 &&
                    (matcher.group(1).length() == 1 || matcher.group(1).length() == 3))
                lineList = lineList + "[" + matcher.group(1) + "] ";
        }
        return "Line is not directly affected, please see " + lineList + "for more info.";
    }

    private SubwayLineInfo buildSubwayLineInfo(String line, String lineGroup) {
        return subwayLineInfo = subwayLineInfo.builder()
                .status(extractPattern(lineGroup, "<status>(.*?)</status>"))
                .statusMessage(checkStatusAppliesToLine(line, cleanupTextSegment(extractTextSegment(lineGroup))))
                .statusDate(extractPattern(lineGroup, "<Date>(.*?)</Date>"))
                .statusTime(extractPattern(lineGroup, "<Time>(.*?)</Time>"))
                .build();
    }
}
