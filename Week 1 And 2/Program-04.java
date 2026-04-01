import java.util.*;

class PlagiarismDetector {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private final int N = 5;

    public void indexDocument(String docId, String content) {
        String[] words = content.split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            String ngram = String.join(" ",
                    Arrays.copyOfRange(words, i, i + N));

            ngramIndex
                .computeIfAbsent(ngram, k -> new HashSet<>())
                .add(docId);
        }
    }

    public double analyzeDocument(String docId, String content) {
        String[] words = content.split("\\s+");
        int totalNgrams = words.length - N + 1;
        Map<String, Integer> matchCount = new HashMap<>();

        for (int i = 0; i <= words.length - N; i++) {
            String ngram = String.join(" ",
                    Arrays.copyOfRange(words, i, i + N));

            if (ngramIndex.containsKey(ngram)) {
                for (String otherDoc : ngramIndex.get(ngram)) {
                    if (!otherDoc.equals(docId))
                        matchCount.merge(otherDoc, 1, Integer::sum);
                }
            }
        }

        for (String otherDoc : matchCount.keySet()) {
            double similarity =
                (matchCount.get(otherDoc) * 100.0) / totalNgrams;
            System.out.println("Similarity with " +
                    otherDoc + ": " + similarity + "%");
        }

        return 0;
    }
}
