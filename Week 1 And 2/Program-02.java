import java.util.*;
import java.util.concurrent.*;

class FlashSaleInventory {

    private ConcurrentHashMap<String, Integer> stockMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Queue<Integer>> waitingList = new ConcurrentHashMap<>();

    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new ConcurrentLinkedQueue<>());
    }

    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {
        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, remaining: " + (stock - 1);
        } else {
            waitingList.get(productId).offer(userId);
            return "Added to waiting list, position #" +
                    waitingList.get(productId).size();
        }
    }
}
