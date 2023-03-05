import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class EventsStatisticImpl implements EventsStatistic {

    private final Clock clock;
    private final Map<String, List<Instant>> statistic;

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
        this.statistic = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        if (!statistic.containsKey(name)) {
            ArrayList<Instant> nameList = new ArrayList<>();
            nameList.add(clock.now());
            statistic.put(name, nameList);
        } else {
            statistic.get(name).add(clock.now());
        }
    }
    @Override
    public double getEventStatisticByName(String name) {
        Instant hourBefore = clock.now().minus(Duration.ofHours(1));
        List<Instant> instantsByName = statistic.get(name);
        if (instantsByName == null) {
            statistic.put(name, new ArrayList<>());
            return 0;
        } else {
            return instantsByName.stream().filter(instant -> instant.isAfter(hourBefore)).count() / 60.0;
        }
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        HashMap<String, Double> answer = new HashMap<>();
        Instant hourBefore = clock.now().minus(Duration.ofHours(1));
        for (String key : statistic.keySet()) {
            double keyResult = statistic.get(key)
                    .stream().filter(instant -> instant.isAfter(hourBefore)).count() / 60.0;
            answer.put(key, keyResult);
        }
        return answer;
    }


    @Override
    public void printStatistic() {
        Map<String, Double> stats = getAllEventStatistic();
        for (String key : stats.keySet()) {
            System.out.println(key + ": " + stats.get(key));
        }
    }
}
