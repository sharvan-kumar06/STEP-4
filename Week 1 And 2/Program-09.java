import java.util.*;

class Transaction {
    int id, amount;
    String merchant;
    long time;
}

class FraudDetector {

    public List<int[]> twoSum(List<Transaction> txs, int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : txs) {
            int need = target - t.amount;
            if (map.containsKey(need))
                result.add(new int[]{map.get(need).id, t.id});
            map.put(t.amount, t);
        }
        return result;
    }

    public Map<Integer, List<Transaction>> detectDuplicates(List<Transaction> txs) {
        Map<Integer, List<Transaction>> map = new HashMap<>();
        for (Transaction t : txs)
            map.computeIfAbsent(t.amount, k -> new ArrayList<>()).add(t);
        return map;
    }
}
