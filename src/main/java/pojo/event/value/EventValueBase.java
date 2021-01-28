package pojo.event.value;

import enums.EventValueType;
import pojo.Message;

/**
 * Base class of RowChange/DDL/Resolve event
 */
public class EventValueBase {

    // Ticdc event attributes. see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#restrictions
    private EventValueType type;
    // MQ partition
//    private int kafkaPartition;
//    private long kafkaOffset;
//    private long kafkaTimestamp;

    public EventValueBase() {
    }

    public EventValueBase(EventValueType type, Message kafkaMessage) {
        this.type = type;
//        this.kafkaPartition = kafkaMessage.getPartition();
//        this.kafkaOffset = kafkaMessage.getOffset();
//        this.kafkaTimestamp = kafkaMessage.getTimestamp();
    }

    public EventValueType getType() {
        return type;
    }

    public void setType(EventValueType type) {
        this.type = type;
    }

//    public int getKafkaPartition() {
//        return kafkaPartition;
//    }
//
//    public void setKafkaPartition(int kafkaPartition) {
//        this.kafkaPartition = kafkaPartition;
//    }
//
//    public long getKafkaOffset() {
//        return kafkaOffset;
//    }
//
//    public void setKafkaOffset(long kafkaOffset) {
//        this.kafkaOffset = kafkaOffset;
//    }
//
//    public long getKafkaTimestamp() {
//        return kafkaTimestamp;
//    }
//
//    public void setKafkaTimestamp(long kafkaTimestamp) {
//        this.kafkaTimestamp = kafkaTimestamp;
//    }
}



