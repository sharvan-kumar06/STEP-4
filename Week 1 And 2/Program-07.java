import java.util.*;

class AutocompleteSystem {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> freqMap = new HashMap<>();
    }

    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> globalFreq = new HashMap<>();

    public void insert(String query) {
        globalFreq.put(query, globalFreq.getOrDefault(query, 0) + 1);

        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
            node.freqMap.put(query, globalFreq.get(query));
        }
    }

    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return List.of();
            node = node.children.get(c);
        }

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        for (var e : node.freqMap.entrySet()) {
            pq.offer(e);
            if (pq.size() > 10) pq.poll();
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll().getKey());
        Collections.reverse(result);
        return result;
    }
}
