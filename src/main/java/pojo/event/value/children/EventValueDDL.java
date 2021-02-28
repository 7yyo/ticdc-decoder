package pojo.event.value.children;

import pojo.Message;
import enums.EventValueType;
import pojo.event.value.EventValueBase;

/**
 * The DDL event
 * See detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-event
 */
public class EventValueDDL extends EventValueBase {

    private String q;
    private int t;

    public EventValueDDL(Message message) {
        super(EventValueType.ddl, message);
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
