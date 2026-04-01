import java.util.*;

class DNSCache {

    class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int MAX_SIZE = 100;
    private LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0, misses = 0;

    public DNSCache() {
        cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_SIZE;
            }
        };
    }

    public String resolve(String domain) {
        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);
            if (!entry.isExpired()) {
                hits++;
                return entry.ip;
            }
            cache.remove(domain);
        }

        misses++;
        String newIP = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(newIP, 300));
        return newIP;
    }

    private String queryUpstreamDNS(String domain) {
        return "192.168.1." + new Random().nextInt(255);
    }

    public void getStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}
