import java.util.concurrent.*;

class RateLimiter {

    static class TokenBucket {
        int tokens;
        long lastRefillTime;

        TokenBucket(int capacity) {
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    private final int MAX_TOKENS = 1000;
    private final long REFILL_INTERVAL = 3600_000; // 1 hour
    private ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public synchronized String checkRateLimit(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(
                clientId, k -> new TokenBucket(MAX_TOKENS));

        long now = System.currentTimeMillis();

        if (now - bucket.lastRefillTime >= REFILL_INTERVAL) {
            bucket.tokens = MAX_TOKENS;
            bucket.lastRefillTime = now;
        }

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " remaining)";
        }

        long retryAfter = (REFILL_INTERVAL - (now - bucket.lastRefillTime)) / 1000;
        return "Denied (retry after " + retryAfter + "s)";
    }
}
