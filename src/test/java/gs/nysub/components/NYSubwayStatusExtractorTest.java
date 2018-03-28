package gs.nysub.components;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class NYSubwayStatusExtractorTest {

    private NYSubwayStatusExtractor nySubwayStatusExtractor;

    private String status;

    @Before
    public void setup() throws IOException {
        nySubwayStatusExtractor = new NYSubwayStatusExtractor();
        setupStatusInput();
    }

    @Test
    public void givenASubwayLineThatDoesNotExist_returnNotFound() throws IOException {
        String status = nySubwayStatusExtractor.getLineStatus("X");

        assertEquals(status, "Line not found");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("7");

        assertEquals(lineStatus, "*GOOD SERVICE*");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine_forALineThatIsGrouped() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("4");

        assertEquals(lineStatus, "*GOOD SERVICE*");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine_forTheSIRLine() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("SIR");

        assertEquals(lineStatus, "*GOOD SERVICE*");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine_whichHasDelays() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("G");

        assertEquals(lineStatus, "*DELAYS* 03/13/2017 11:49PM\nDelays Posted: 03/13/2017 11:49PM Due to FDNY Activity at 4 Av-9 St, southbound [F] and [G] trains are running with delays. Allow additional travel time.");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine_whichHasADirectServiceChange() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("E");

        assertEquals(lineStatus, "*SERVICE CHANGE* 03/13/2017 11:42PM\nService Change Posted: 03/13/2017 11:42PM Due to NYC Transit Cold Weather Plan, the following service changes are in effect: [E] trains are running local between Queens Plaza and Forest Hills-71 Av in both directions. [F] trains are running local between 21 St-Queensbridge and Forest Hills-71 Av in both directions. Allow additional travel time.");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine_whichHasNoDirectServiceChange() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("A");

        assertEquals(lineStatus, "*SERVICE CHANGE* 03/13/2017 11:42PM\nLine is not directly affected, please see [E] [F] for more info.");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheRequestedLine_whichHasNoDirectServiceChange_butHasRepeatedOtherLines() throws IOException {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("B");

        assertEquals(lineStatus, "*DELAYS* 03/13/2017 11:49PM\nLine is not directly affected, please see [F] [G] [E] [D] for more info.");
    }

    @Test
    public void givenAllServicesStatusAsAString_extractStatus_returnStatusOfTheMissingLineW() {
        nySubwayStatusExtractor.extractStatusInfo(status);

        String lineStatus = nySubwayStatusExtractor.getLineStatus("W");

        assertEquals(lineStatus, "*GOOD SERVICE*");
    }

    private void setupStatusInput() throws IOException {
        status = new String(
            Files.readAllBytes(
                new File(
                    this.getClass().getResource("/SampleStatusResponse.txt").getFile()
                ).toPath()
            )
        );
    }
}