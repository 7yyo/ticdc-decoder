package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

/**
 * @TicdcEventBase.java 定义了三种 event 的父类，三种 event 分别是
 * @Row_Changed Event
 * @DDl Event
 * @Resolved Event
 *
 * 三种 event 的说明请参考 https://docs.pingcap.com/zh/tidb/stable/ticdc-open-protocol 的概述
 */
public class TicdcEventBase {

    // Ticdc event 的基本属性
    private TicdcEventType type;
    private int kafkaPartition;
    private long kafkaOffset;
    private long kafkaTimestamp;

    public TicdcEventBase() {
    }

    public TicdcEventBase(TicdcEventType type, KafkaMessage kafkaMessage) {
        this.type = type;
        this.kafkaPartition = kafkaMessage.getPartition();
        this.kafkaOffset = kafkaMessage.getOffset();
        this.kafkaTimestamp = kafkaMessage.getTimestamp();

    }

    public TicdcEventType getType() {
        return type;
    }

    public void setType(TicdcEventType type) {
        this.type = type;
    }

    public int getKafkaPartition() {
        return kafkaPartition;
    }

    public void setKafkaPartition(int kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }

    public long getKafkaOffset() {
        return kafkaOffset;
    }

    public void setKafkaOffset(long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }

    public long getKafkaTimestamp() {
        return kafkaTimestamp;
    }

    public void setKafkaTimestamp(long kafkaTimestamp) {
        this.kafkaTimestamp = kafkaTimestamp;
    }
}



