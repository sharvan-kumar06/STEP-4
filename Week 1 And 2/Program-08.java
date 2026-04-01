import java.util.*;

class ParkingLot {

    enum Status { EMPTY, OCCUPIED, DELETED }

    static class Slot {
        String plate;
        Status status = Status.EMPTY;
        long entryTime;
    }

    private Slot[] table = new Slot[500];

    public ParkingLot() {
        for (int i = 0; i < table.length; i++)
            table[i] = new Slot();
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % table.length;
    }

    public int parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % table.length;
            probes++;
        }

        table[index].plate = plate;
        table[index].status = Status.OCCUPIED;
        table[index].entryTime = System.currentTimeMillis();

        System.out.println("Probes: " + probes);
        return index;
    }

    public void exitVehicle(String plate) {
        int index = hash(plate);

        while (table[index].status != Status.EMPTY) {
            if (plate.equals(table[index].plate)) {
                long duration = System.currentTimeMillis() - table[index].entryTime;
                table[index].status = Status.DELETED;
                System.out.println("Duration: " + duration / 60000 + " min");
                return;
            }
            index = (index + 1) % table.length;
        }
    }
}
