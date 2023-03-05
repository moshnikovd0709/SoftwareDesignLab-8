import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventsStatisticTest {

    private static final double delta = 1e-7;

    InstallableClock clock;
    EventsStatistic statistic;

    @BeforeEach
    public void preSetUp() {
        clock = new InstallableClock(Instant.now());
        statistic = new EventsStatisticImpl(clock);
    }

    @Test
    public void testEmpty() {
        assertEquals(0, statistic.getEventStatisticByName("empty-test"), delta);
    }

    @Test
    public void testOneEvent() {
        statistic.incEvent("testOne");
        statistic.incEvent("testOne");
        statistic.incEvent("testOne");
        statistic.incEvent("testOne");
        statistic.incEvent("testOne");

        assertEquals(5.0 / 60, statistic.getEventStatisticByName("testOne"), delta);
        assertEquals(0, statistic.getEventStatisticByName("empty"), delta);
    }

    @Test
    public void testOneEventWithTickForward() {
        statistic.incEvent("testTick");
        tickForward(Duration.ofHours(2));

        assertEquals(0, statistic.getEventStatisticByName("testTick"), delta);
    }

    @Test
    public void testSingleEventWithSeveralTickForward() {
        statistic.incEvent("test");
        tickForward(Duration.ofMinutes(30));
        statistic.incEvent("test");
        tickForward(Duration.ofMinutes(30));
        statistic.incEvent("test");

        assertEquals(2.0 / 60, statistic.getEventStatisticByName("test"), delta);
    }

    @Test
    public void testSeveralEvents() {
        statistic.incEvent("test0");
        statistic.incEvent("test1");
        tickForward(Duration.ofMinutes(20));
        statistic.incEvent("test0");
        statistic.incEvent("test2");
        tickForward(Duration.ofMinutes(20));
        statistic.incEvent("test0");
        statistic.incEvent("test1");
        tickForward(Duration.ofMinutes(20));
        statistic.incEvent("test0");
        statistic.incEvent("test1");

        Map<String, Double> stats = statistic.getAllEventStatistic();
        assertEquals(3.0 / 60, stats.get("test0"), delta);
        assertEquals(2.0 / 60, stats.get("test1"), delta);
        assertEquals(1.0 / 60, stats.get("test2"), delta);
    }

    private void tickForward(Duration duration) {
        clock.setNow(clock.now().plus(duration));
    }

}
