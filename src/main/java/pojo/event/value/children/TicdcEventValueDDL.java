package pojo.event.value.children;

import pojo.event.value.TicdcEventValueBase;
import pojo.KafkaMessage;
import enums.TicdcEventValueType;

/**
 * The DDL event
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-event
 */
public class TicdcEventValueDDL extends TicdcEventValueBase {

    // DDL statement
    private String q;
    // DDL type. see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-type-code
    private int t;

    public TicdcEventValueDDL(KafkaMessage kafkaMessage) {
        super(TicdcEventValueType.ddl, kafkaMessage);
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }
}
