package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

/**
 * @TicdcEventResolve.java 定义了 Resolve 类型的 event：
 * 代表一个特殊的时间点，表示在这个时间点前的收到的 Event 是完整的
 */
public class TicdcEventResolve extends TicdcEventBase {

    public TicdcEventResolve(KafkaMessage kafkaMessage) {
        super(TicdcEventType.resolved, kafkaMessage);
    }

}
