import java.util.*;

class MultiLevelCache {

    private LinkedHashMap<String, String> L1 =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> e) {
                    return size() > 10000;
                }
            };

    private LinkedHashMap<String, String> L2 =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> e) {
                    return size() > 100000;
                }
            };

    private Map<String, String> database = new HashMap<>();

    public String getVideo(String id) {
        if (L1.containsKey(id)) return L1.get(id);
        if (L2.containsKey(id)) {
            String data = L2.get(id);
            L1.put(id, data);
            return data;
        }
        String data = database.get(id);
        L2.put(id, data);
        return data;
    }
}
