package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

/**
 * @TicdcEventDDL.java 定义了 DDL 类型的 event：
 * 代表 DDL 变更，在上游成功执行 DDL 后发出，DDL Event 会广播到每一个 MQ Partition 中
 */
public class TicdcEventDDL extends TicdcEventBase {

    private String q; // ddl 语句
    private int t; // ddl 的类型，如 creat、drop 等,详细可见 see: https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-type-code

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
