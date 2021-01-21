package ticdcJavaDemo;

public class KafkaMessage {

    // Key
    private byte[] key;
    // Value
    private byte[] value;
    // MQ Partition
    private int partition;
    // Binary offset
    private long offset;
    // timestamp
    private long timestamp;

    public KafkaMessage() {
    }

    // KafkaMessage has Key & Value
    public KafkaMessage(byte[] key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
