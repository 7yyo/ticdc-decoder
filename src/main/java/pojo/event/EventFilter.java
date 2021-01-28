package pojo.event;

import java.util.HashMap;
import java.util.Map;

public class EventFilter {

    // tableName_partition -> Max TS from Row Change Event
    private Map<String, Long> tableMaxTSMap = new HashMap<>();
    // partition -> Max ts from Resolve Event
    private Map<Integer, Long> resolveMaxTSMap = new HashMap<>();

    /**
     * Check for duplicated row change event.
     *
     * @param tableName   Table name
     * @param partition   Kafka topic partition
     * @param rowChangeTS TS
     * @return Return false if the event is duplicated.
     */
    public boolean check(String tableName, int partition, long rowChangeTS) {
        String mapKey = tableName + "_" + partition;
        Long rowChangeMaxTS = tableMaxTSMap.get(mapKey);
        if (rowChangeMaxTS == null) {
            tableMaxTSMap.put(mapKey, rowChangeMaxTS);
        } else {
            if (rowChangeTS <= rowChangeMaxTS) {
                return false;
            }
            tableMaxTSMap.put(mapKey, rowChangeTS);
        }

        Long resolveMaxTS = resolveMaxTSMap.get(partition);
        if (rowChangeMaxTS != null) {
            if (rowChangeTS <= resolveMaxTS) {
                return false;
            }
        }
        return true;
    }

    /**
     * Record the max resolve TS when receive "Resolved Event"
     *
     * @param partition Kafka topic partition
     * @param resolveTS Resolve TS
     */
    public void resolveEvent(int partition, long resolveTS) {
        resolveMaxTSMap.put(partition, resolveTS);
    }
}
