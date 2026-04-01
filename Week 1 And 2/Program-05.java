import java.util.*;
import java.util.concurrent.*;

class AnalyticsDashboard {

    private ConcurrentHashMap<String, Integer> pageViews = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> trafficSource = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {

        pageViews.merge(url, 1, Integer::sum);

        uniqueVisitors
            .computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet())
            .add(userId);

        trafficSource.merge(source, 1, Integer::sum);
    }

    public void getDashboard() {

        System.out.println("Top Pages:");

        pageViews.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(10)
                .forEach(entry -> {
                    String url = entry.getKey();
                    int views = entry.getValue();
                    int unique = uniqueVisitors.get(url).size();
                    System.out.println(url + " - " +
                            views + " views (" +
                            unique + " unique)");
                });

        System.out.println("Traffic Sources:");
        trafficSource.forEach((k, v) ->
                System.out.println(k + ": " + v));
    }
}
