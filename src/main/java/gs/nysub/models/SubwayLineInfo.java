package gs.nysub.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubwayLineInfo {

    private String status;
    private String statusMessage;
    private String statusDate;
    private String statusTime;
}
