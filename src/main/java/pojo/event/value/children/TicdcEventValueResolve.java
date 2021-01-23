package pojo.event.value.children;

import pojo.event.value.TicdcEventValueBase;
import pojo.KafkaMessage;
import enums.TicdcEventValueType;

/**
 * The resolve event
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-type-code
 */
public class TicdcEventValueResolve extends TicdcEventValueBase {

    public TicdcEventValueResolve(KafkaMessage kafkaMessage) {
        super(TicdcEventValueType.resolved, kafkaMessage);
    }

}
