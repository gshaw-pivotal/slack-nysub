package gs.nysub;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SubwayLines {

    private String[] subwayLines = {"1", "2", "3", "4", "5", "6", "7", "A", "B", "C", "D", "E", "F", "G", "J", "L", "M", "N", "Q", "R", "S", "SIR", "W", "Z"};

    public boolean isValidLine(String line) {
        return Arrays.binarySearch(subwayLines, line) >= 0;
    }

    public String[] getSubwayLines() {
        return subwayLines;
    }
}
