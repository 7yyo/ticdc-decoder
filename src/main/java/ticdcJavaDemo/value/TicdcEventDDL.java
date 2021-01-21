package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

public class TicdcEventDDL extends TicdcEventBase {

    private String q; // DDL query SQL
    private int t; // DDL type code, see: https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-type-code

    public TicdcEventDDL(KafkaMessage kafkaMessage) {
        super(TicdcEventType.ddl, kafkaMessage);
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
