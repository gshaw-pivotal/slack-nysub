package gs.nysub.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UptimeActuatorTest {

    @Mock
    private MetricsEndpoint metricsEndpoint;

    private UptimeActuator uptimeActuator;

    private long uptime = 200;

    @Before
    public void setup() {
        uptimeActuator = new UptimeActuator(metricsEndpoint);

        HashMap<String, Object> metrics = new HashMap<>();
        metrics.put("uptime", uptime);
        when(metricsEndpoint.invoke()).thenReturn(metrics);
    }

    @Test
    public void requestUptimeMetric_getUptimeValue() {
        assertTrue(uptimeActuator.getBotUptime() == uptime);

        verify(metricsEndpoint).invoke();
    }
}