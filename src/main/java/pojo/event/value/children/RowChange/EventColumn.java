package pojo.event.value.children.RowChange;

/**
 * Column in ticdc event
 * See detail. https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#row-changed-event
 */
public class EventColumn {

    private String name;
    // column type. see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#column-type-code
    private int t;
    // where handle
    private boolean h;
    // flag. see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#bit-flags-of-columns
    private int f;
    // column value
    private Object v;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }
}