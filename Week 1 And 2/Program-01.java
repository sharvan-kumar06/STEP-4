import java.util.*;
import java.util.concurrent.*;

class UsernameChecker {

    private ConcurrentHashMap<String, Integer> usernameMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> attemptFrequency = new ConcurrentHashMap<>();

    // Check availability
    public boolean checkAvailability(String username) {
        attemptFrequency.merge(username, 1, Integer::sum);
        return !usernameMap.containsKey(username);
    }

    // Register username
    public void register(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }
        suggestions.add(username.replace("_", "."));
        return suggestions;
    }

    // Most attempted username
    public String getMostAttempted() {
        return Collections.max(attemptFrequency.entrySet(),
                Map.Entry.comparingByValue()).getKey();
    }
}
