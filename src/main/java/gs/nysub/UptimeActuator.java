package gs.nysub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.stereotype.Component;

@Component
public class UptimeActuator {

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    @Autowired
    public UptimeActuator(MetricsEndpoint metricsEndpoint) {
        this.metricsEndpoint = metricsEndpoint;
    }

    public long getBotUptime() {
        return (Long) metricsEndpoint.invoke().get("uptime");
    }
}
