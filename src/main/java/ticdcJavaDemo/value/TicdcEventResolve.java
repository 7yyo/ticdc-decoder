package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

public class TicdcEventResolve extends TicdcEventBase {

    public TicdcEventResolve(KafkaMessage kafkaMessage) {
        super(TicdcEventType.resolved, kafkaMessage);
    }

}
